/*
 * Copyright © 2004-2024 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.LoginServerThread;
import com.l2jserver.gameserver.data.xml.impl.SecondaryAuthData;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.serverpackets.Ex2ndPasswordAck;
import com.l2jserver.gameserver.network.serverpackets.Ex2ndPasswordCheck;
import com.l2jserver.gameserver.network.serverpackets.Ex2ndPasswordVerify;
import com.l2jserver.gameserver.util.Util;

/**
 * Secondary Password Auth.
 * @author mrTJO
 */
public class SecondaryPasswordAuth {
	
	private static final Logger LOG = LoggerFactory.getLogger(SecondaryPasswordAuth.class);
	
	private final L2GameClient _activeClient;
	
	private String _password;
	private int _wrongAttempts;
	private boolean _authed;
	
	private static final String VAR_PWD = "secauth_pwd";
	private static final String VAR_WTE = "secauth_wte";
	
	private static final String SELECT_PASSWORD = "SELECT var, value FROM account_gsdata WHERE account_name=? AND var LIKE 'secauth_%'";
	private static final String INSERT_PASSWORD = "INSERT INTO account_gsdata VALUES (?, ?, ?)";
	private static final String UPDATE_PASSWORD = "UPDATE account_gsdata SET value=? WHERE account_name=? AND var=?";
	
	private static final String INSERT_ATTEMPT = "INSERT INTO account_gsdata VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value=?";
	
	public SecondaryPasswordAuth(L2GameClient activeClient) {
		_activeClient = activeClient;
		_password = null;
		_wrongAttempts = 0;
		_authed = false;
		loadPassword();
	}
	
	private void loadPassword() {
		String var, value;
		try (var con = ConnectionFactory.getInstance().getConnection();
			var statement = con.prepareStatement(SELECT_PASSWORD)) {
			statement.setString(1, _activeClient.getAccountName());
			try (var rs = statement.executeQuery()) {
				while (rs.next()) {
					var = rs.getString("var");
					value = rs.getString("value");
					
					if (var.equals(VAR_PWD)) {
						_password = value;
					} else if (var.equals(VAR_WTE)) {
						_wrongAttempts = Integer.parseInt(value);
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("Error while reading password.", ex);
		}
	}
	
	public boolean savePassword(String password) {
		if (passwordExist()) {
			LOG.warn(_activeClient.getAccountName() + " forced save password!");
			_activeClient.closeNow();
			return false;
		}
		
		if (!validatePassword(password)) {
			_activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
			return false;
		}
		
		password = cryptPassword(password);
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_PASSWORD)) {
			ps.setString(1, _activeClient.getAccountName());
			ps.setString(2, VAR_PWD);
			ps.setString(3, password);
			ps.execute();
		} catch (Exception ex) {
			LOG.error("Error while writing password!", ex);
			return false;
		}
		_password = password;
		return true;
	}
	
	public boolean insertWrongAttempt(int attempts) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_ATTEMPT)) {
			ps.setString(1, _activeClient.getAccountName());
			ps.setString(2, VAR_WTE);
			ps.setString(3, Integer.toString(attempts));
			ps.setString(4, Integer.toString(attempts));
			ps.execute();
		} catch (Exception ex) {
			LOG.error("Error while writing wrong attempts!", ex);
			return false;
		}
		return true;
	}
	
	public boolean changePassword(String oldPassword, String newPassword) {
		if (!passwordExist()) {
			LOG.warn(_activeClient.getAccountName() + " forced change password");
			_activeClient.closeNow();
			return false;
		}
		
		if (!checkPassword(oldPassword, true)) {
			return false;
		}
		
		if (!validatePassword(newPassword)) {
			_activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
			return false;
		}
		
		newPassword = cryptPassword(newPassword);
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(UPDATE_PASSWORD)) {
			ps.setString(1, newPassword);
			ps.setString(2, _activeClient.getAccountName());
			ps.setString(3, VAR_PWD);
			ps.execute();
		} catch (Exception ex) {
			LOG.error("Error while reading password!", ex);
			return false;
		}
		
		_password = newPassword;
		_authed = false;
		return true;
	}
	
	public boolean checkPassword(String password, boolean skipAuth) {
		password = cryptPassword(password);
		
		if (!password.equals(_password)) {
			_wrongAttempts++;
			if (_wrongAttempts < SecondaryAuthData.getInstance().getMaxAttempts()) {
				_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_WRONG, _wrongAttempts));
				insertWrongAttempt(_wrongAttempts);
			} else {
				final var accountName = _activeClient.getAccountName();
				final var hostAddress = _activeClient.getConnectionAddress().getHostAddress();
				final var banTime = SecondaryAuthData.getInstance().getBanTime();
				LoginServerThread.getInstance().sendTempBan(accountName, hostAddress, banTime);
				final var recoveryLink = SecondaryAuthData.getInstance().getRecoveryLink();
				LoginServerThread.getInstance().sendMail(accountName, "SATempBan", hostAddress, Integer.toString(_wrongAttempts), Long.toString(banTime), recoveryLink);
				LOG.warn(_activeClient.getAccountName() + " - ({}) has inputted the wrong password {} times in row.", hostAddress, _wrongAttempts);
				insertWrongAttempt(0);
				_activeClient.close(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_BAN, SecondaryAuthData.getInstance().getMaxAttempts()));
			}
			return false;
		}
		if (!skipAuth) {
			_authed = true;
			_activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_OK, _wrongAttempts));
		}
		insertWrongAttempt(0);
		return true;
	}
	
	public boolean passwordExist() {
		return _password != null;
	}
	
	public void openDialog() {
		if (passwordExist()) {
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_PROMPT));
		} else {
			_activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_NEW));
		}
	}
	
	public boolean isAuthed() {
		return _authed;
	}
	
	private String cryptPassword(String password) {
		try {
			final var md = MessageDigest.getInstance("SHA");
			byte[] raw = password.getBytes(StandardCharsets.UTF_8);
			byte[] hash = md.digest(raw);
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException ex) {
			LOG.error("Unsupported Algorithm!", ex);
		}
		return null;
	}
	
	private boolean validatePassword(String password) {
		if (!Util.isDigit(password)) {
			return false;
		}
		
		if ((password.length() < 6) || (password.length() > 8)) {
			return false;
		}
		return !SecondaryAuthData.getInstance().isForbiddenPassword(password);
	}
}