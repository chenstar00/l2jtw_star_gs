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
package com.l2jserver.gameserver;

import static com.l2jserver.gameserver.config.Configuration.customs;
import static com.l2jserver.gameserver.config.Configuration.database;
import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.geodata;
import static com.l2jserver.gameserver.config.Configuration.hexId;
import static com.l2jserver.gameserver.config.Configuration.mmo;
import static com.l2jserver.gameserver.config.Configuration.server;
import static com.l2jserver.gameserver.config.Configuration.telnet;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.UPnPService;
import com.l2jserver.commons.dao.ServerNameDAO;
import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.commons.util.IPv4Filter;
import com.l2jserver.commons.util.Util;
import com.l2jserver.gameserver.bbs.service.ForumsBBSManager;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.dao.factory.impl.DAOFactory;
import com.l2jserver.gameserver.data.json.ExperienceData;
import com.l2jserver.gameserver.data.sql.impl.AnnouncementsTable;
import com.l2jserver.gameserver.data.sql.impl.CharNameTable;
import com.l2jserver.gameserver.data.sql.impl.CharSummonTable;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.data.sql.impl.CrestTable;
import com.l2jserver.gameserver.data.sql.impl.NpcBufferTable;
import com.l2jserver.gameserver.data.sql.impl.OfflineTradersTable;
import com.l2jserver.gameserver.data.sql.impl.SummonSkillsTable;
import com.l2jserver.gameserver.data.sql.impl.TeleportLocationTable;
import com.l2jserver.gameserver.data.xml.impl.AdminData;
import com.l2jserver.gameserver.data.xml.impl.ArmorSetsData;
import com.l2jserver.gameserver.data.xml.impl.BuyListData;
import com.l2jserver.gameserver.data.xml.impl.CategoryData;
import com.l2jserver.gameserver.data.xml.impl.ClassListData;
import com.l2jserver.gameserver.data.xml.impl.DoorData;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemData;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemGroupsData;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemHPBonusData;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemOptionsData;
import com.l2jserver.gameserver.data.xml.impl.EnchantSkillGroupsData;
import com.l2jserver.gameserver.data.xml.impl.FishData;
import com.l2jserver.gameserver.data.xml.impl.FishingMonstersData;
import com.l2jserver.gameserver.data.xml.impl.FishingRodsData;
import com.l2jserver.gameserver.data.xml.impl.HennaData;
import com.l2jserver.gameserver.data.xml.impl.HitConditionBonusData;
import com.l2jserver.gameserver.data.xml.impl.InitialEquipmentData;
import com.l2jserver.gameserver.data.xml.impl.InitialShortcutData;
import com.l2jserver.gameserver.data.xml.impl.KarmaData;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.data.xml.impl.OptionData;
import com.l2jserver.gameserver.data.xml.impl.PetDataTable;
import com.l2jserver.gameserver.data.xml.impl.PlayerCreationPointData;
import com.l2jserver.gameserver.data.xml.impl.PlayerTemplateData;
import com.l2jserver.gameserver.data.xml.impl.PlayerXpPercentLostData;
import com.l2jserver.gameserver.data.xml.impl.RecipeData;
import com.l2jserver.gameserver.data.xml.impl.SecondaryAuthData;
import com.l2jserver.gameserver.data.xml.impl.SiegeScheduleData;
import com.l2jserver.gameserver.data.xml.impl.SkillLearnData;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.data.xml.impl.StaticObjectData;
import com.l2jserver.gameserver.data.xml.impl.TransformData;
import com.l2jserver.gameserver.data.xml.impl.UIData;
import com.l2jserver.gameserver.datatables.AugmentationData;
import com.l2jserver.gameserver.datatables.BotReportTable;
import com.l2jserver.gameserver.datatables.EventDroplist;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.datatables.MerchantPriceConfigTable;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.handler.EffectHandler;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.instancemanager.AirShipManager;
import com.l2jserver.gameserver.instancemanager.AntiFeedManager;
import com.l2jserver.gameserver.instancemanager.AuctionManager;
import com.l2jserver.gameserver.instancemanager.BoatManager;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.CastleManorManager;
import com.l2jserver.gameserver.instancemanager.ClanHallManager;
import com.l2jserver.gameserver.instancemanager.ClanHallSiegeManager;
import com.l2jserver.gameserver.instancemanager.CoupleManager;
import com.l2jserver.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jserver.gameserver.instancemanager.DayNightSpawnManager;
import com.l2jserver.gameserver.instancemanager.DimensionalRiftManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.FortSiegeManager;
import com.l2jserver.gameserver.instancemanager.FourSepulchersManager;
import com.l2jserver.gameserver.instancemanager.GlobalVariablesManager;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.instancemanager.ItemAuctionManager;
import com.l2jserver.gameserver.instancemanager.ItemsOnGroundManager;
import com.l2jserver.gameserver.instancemanager.MailManager;
import com.l2jserver.gameserver.instancemanager.MapRegionManager;
import com.l2jserver.gameserver.instancemanager.MercTicketManager;
import com.l2jserver.gameserver.instancemanager.PetitionManager;
import com.l2jserver.gameserver.instancemanager.PunishmentManager;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.instancemanager.RaidBossPointsManager;
import com.l2jserver.gameserver.instancemanager.RaidBossSpawnManager;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.instancemanager.WalkingManager;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.AutoSpawnHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.PartyMatchRoomList;
import com.l2jserver.gameserver.model.PartyMatchWaitingList;
import com.l2jserver.gameserver.model.entity.Hero;
import com.l2jserver.gameserver.model.entity.TvTManager;
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.olympiad.Olympiad;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.L2GamePacketHandler;
import com.l2jserver.gameserver.pathfinding.PathFinding;
import com.l2jserver.gameserver.script.faenor.FaenorScriptEngine;
import com.l2jserver.gameserver.scripting.ScriptEngineManager;
import com.l2jserver.gameserver.status.Status;
import com.l2jserver.gameserver.taskmanager.KnownListUpdateTaskManager;
import com.l2jserver.gameserver.taskmanager.TaskManager;
import com.l2jserver.gameserver.util.DeadLockDetector;
import com.l2jserver.mmocore.SelectorConfig;
import com.l2jserver.mmocore.SelectorThread;

public final class GameServer {
	
	private static final Logger LOG = LoggerFactory.getLogger(GameServer.class);
	
	private static final String DATAPACK = "-dp";
	
	private static final String SCRIPT = "-s";
	
	private static final String GEODATA = "-gd";
	
	private final SelectorThread<L2GameClient> _selectorThread;
	
	private final L2GamePacketHandler _gamePacketHandler;
	
	private final DeadLockDetector _deadDetectThread;
	
	public static GameServer gameServer;
	
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	
	public GameServer() throws Exception {
		
		final var serverLoadStart = startTimedSection("Database");
		ConnectionFactory.builder() //
			.withUrl(database().getURL()) //
			.withUser(database().getUser()) //
			.withPassword(database().getPassword()) //
			.withMaxIdleTime(database().getMaxIdleTime()) //
			.withMaxPoolSize(database().getMaxConnections()) //
			.build();
		
		DAOFactory.getInstance();
		
		if (!IdFactory.getInstance().isInitialized()) {
			LOG.error("Could not read object IDs from database. Please check your configuration.");
			throw new Exception("Could not initialize the Id factory!");
		}
		endTimedSection("Database", serverLoadStart);
		
		ThreadPoolManager.getInstance();
		EventDispatcher.getInstance();
		ScriptEngineManager.getInstance();
		
		final var worldStart = startTimedSection("World");
		GameTimeController.init();
		InstanceManager.getInstance();
		L2World.getInstance();
		MapRegionManager.getInstance();
		AnnouncementsTable.getInstance();
		GlobalVariablesManager.getInstance();
		endTimedSection("World", worldStart);
		
		final var dataStart = startTimedSection("Data");
		CategoryData.getInstance();
		SecondaryAuthData.getInstance();
		endTimedSection("Data", dataStart);
		
		final var effectsStart = startTimedSection("Effects");
		EffectHandler.getInstance().executeScript();
		endTimedSection("Effects", effectsStart);
		
		final var esgStart = startTimedSection("Enchant Skill Groups");
		EnchantSkillGroupsData.getInstance();
		endTimedSection("Enchant Skill Groups", esgStart);
		
		final var skillTreesStart = startTimedSection("Skill Trees");
		SkillTreesData.getInstance();
		endTimedSection("Skill Trees", skillTreesStart);
		
		final var skillsStart = startTimedSection("Skills");
		SkillData.getInstance();
		SummonSkillsTable.getInstance();
		endTimedSection("Skills", skillsStart);
		
		final var itemsStart = startTimedSection("Items");
		ItemTable.getInstance();
		EnchantItemGroupsData.getInstance();
		EnchantItemData.getInstance();
		EnchantItemOptionsData.getInstance();
		OptionData.getInstance();
		EnchantItemHPBonusData.getInstance();
		MerchantPriceConfigTable.getInstance().loadInstances();
		BuyListData.getInstance();
		MultisellData.getInstance();
		RecipeData.getInstance();
		ArmorSetsData.getInstance();
		FishData.getInstance();
		FishingMonstersData.getInstance();
		FishingRodsData.getInstance();
		HennaData.getInstance();
		endTimedSection("Items", itemsStart);
		
		final var charactersStart = startTimedSection("Characters");
		ClassListData.getInstance();
		InitialEquipmentData.getInstance();
		InitialShortcutData.getInstance();
		ExperienceData.getInstance();
		PlayerXpPercentLostData.getInstance();
		KarmaData.getInstance();
		HitConditionBonusData.getInstance();
		PlayerTemplateData.getInstance();
		PlayerCreationPointData.getInstance();
		CharNameTable.getInstance();
		AdminData.getInstance();
		RaidBossPointsManager.getInstance();
		PetDataTable.getInstance();
		CharSummonTable.getInstance().init();
		endTimedSection("Characters", charactersStart);
		
		final var bbsStart = startTimedSection("BBS");
		if (general().enableCommunityBoard()) {
			ForumsBBSManager.getInstance().load();
		}
		endTimedSection("BBS", bbsStart);
		
		final var clansStart = startTimedSection("Clans");
		ClanTable.getInstance();
		ClanHallSiegeManager.getInstance();
		ClanHallManager.getInstance();
		AuctionManager.getInstance();
		endTimedSection("Clans", clansStart);
		
		final var geodataStart = startTimedSection("Geodata");
		GeoData.getInstance();
		if (geodata().getPathFinding() > 0) {
			PathFinding.getInstance();
		}
		endTimedSection("Geodata", geodataStart);
		
		final var npcsStart = startTimedSection("NPCs");
		SkillLearnData.getInstance();
		NpcData.getInstance();
		WalkingManager.getInstance();
		StaticObjectData.getInstance();
		ZoneManager.getInstance();
		DoorData.getInstance();
		CastleManager.getInstance().loadInstances();
		NpcBufferTable.getInstance();
		GrandBossManager.getInstance().initZones();
		EventDroplist.getInstance();
		endTimedSection("NPCs", npcsStart);
		
		final var auctionStart = startTimedSection("Auction Manager");
		ItemAuctionManager.getInstance();
		endTimedSection("Auction Manager", auctionStart);
		
		final var olympiadStart = startTimedSection("Olympiad");
		Olympiad.getInstance();
		Hero.getInstance();
		endTimedSection("Olympiad", olympiadStart);
		
		final var sevenSignsStart = startTimedSection("Seven Signs");
		SevenSigns.getInstance();
		endTimedSection("Seven Signs", sevenSignsStart);
		
		// Call to load caches
		final var cacheStart = startTimedSection("Cache");
		HtmCache.getInstance();
		CrestTable.getInstance();
		TeleportLocationTable.getInstance();
		UIData.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		PetitionManager.getInstance();
		AugmentationData.getInstance();
		CursedWeaponsManager.getInstance();
		TransformData.getInstance();
		BotReportTable.getInstance();
		QuestManager.getInstance();
		BoatManager.getInstance();
		AirShipManager.getInstance();
		GraciaSeedsManager.getInstance();
		endTimedSection("Cache", cacheStart);
		
		final var handlersStart = startTimedSection("Handlers");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/handlers/MasterHandler.java");
		endTimedSection("Handlers", handlersStart);
		
		final var aiStart = startTimedSection("AI");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/ai/AILoader.java");
		endTimedSection("AI", aiStart);
		
		final var instancesStart = startTimedSection("Instances");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/instances/InstanceLoader.java");
		endTimedSection("Instances", instancesStart);
		
		final var graciaStart = startTimedSection("Gracia");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/gracia/GraciaLoader.java");
		endTimedSection("Gracia", graciaStart);
		
		final var hellboundStart = startTimedSection("Hellbound");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/hellbound/HellboundLoader.java");
		endTimedSection("Hellbound", hellboundStart);
		
		final var questsStart = startTimedSection("Quests");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/quests/QuestLoader.java");
		ScriptEngineManager.getInstance().executeScript("com/l2jserver/datapack/quests/TerritoryWarScripts/TerritoryWarSuperClass.java");
		endTimedSection("Quests", questsStart);
		
		final var scriptsStart = startTimedSection("Scripts");
		ScriptEngineManager.getInstance().executeScriptList(new File(server().getDatapackRoot(), "data/scripts.cfg"));
		endTimedSection("Scripts", scriptsStart);
		
		final var spawnsStart = startTimedSection("Spawns");
		SpawnTable.getInstance().load();
		DayNightSpawnManager.getInstance().trim().notifyChangeMode();
		FourSepulchersManager.getInstance().init();
		DimensionalRiftManager.getInstance();
		RaidBossSpawnManager.getInstance();
		endTimedSection("Spawns", spawnsStart);
		
		final var siegeStart = startTimedSection("Siege");
		SiegeManager.getInstance().getSieges();
		CastleManager.getInstance().activateInstances();
		FortManager.getInstance().loadInstances();
		FortManager.getInstance().activateInstances();
		FortSiegeManager.getInstance();
		SiegeScheduleData.getInstance();
		MerchantPriceConfigTable.getInstance().updateReferences();
		TerritoryWarManager.getInstance();
		CastleManorManager.getInstance();
		MercTicketManager.getInstance();
		endTimedSection("Siege", siegeStart);
		
		final var otherStart = startTimedSection("Other");
		if (general().saveDroppedItem()) {
			ItemsOnGroundManager.getInstance();
		}
		
		if ((general().getAutoDestroyDroppedItemAfter() > 0) || (general().getAutoDestroyHerbTime() > 0)) {
			ItemsAutoDestroy.getInstance();
		}
		
		MonsterRace.getInstance();
		SevenSigns.getInstance().spawnSevenSignsNPC();
		SevenSignsFestival.getInstance();
		AutoSpawnHandler.getInstance();
		FaenorScriptEngine.getInstance();
		
		if (customs().allowWedding()) {
			CoupleManager.getInstance();
		}
		
		TaskManager.getInstance();
		
		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.GAME_ID);
		
		if (general().allowMail()) {
			MailManager.getInstance();
		}
		
		PunishmentManager.getInstance();
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		LOG.info("Free Object Ids remaining {}.", IdFactory.getInstance().size());
		
		TvTManager.getInstance();
		KnownListUpdateTaskManager.getInstance();
		
		if ((customs().offlineTradeEnable() || customs().offlineCraftEnable()) && customs().restoreOffliners()) {
			OfflineTradersTable.getInstance().restoreOfflineTraders();
		}
		endTimedSection("Other", otherStart);
		
		if (general().deadLockDetector()) {
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		} else {
			_deadDetectThread = null;
		}
		System.gc();
		// maxMemory is the upper limit the jvm can use, totalMemory the size of
		// the current allocation pool, freeMemory the unused memory in the allocation pool
		long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576;
		long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		LOG.info("Started, free memory {} Mb of {} Mb", freeMem, totalMem);
		Toolkit.getDefaultToolkit().beep();
		LoginServerThread.getInstance().start();
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = mmo().getMaxReadPerPass();
		sc.MAX_SEND_PER_PASS = mmo().getMaxSendPerPass();
		sc.SLEEP_TIME = mmo().getSleepTime();
		sc.HELPER_BUFFER_COUNT = mmo().getHelperBufferCount();
		sc.TCP_NODELAY = mmo().isTcpNoDelay();
		
		_gamePacketHandler = new L2GamePacketHandler();
		_selectorThread = new SelectorThread<>(sc, _gamePacketHandler, _gamePacketHandler, _gamePacketHandler, new IPv4Filter());
		
		InetAddress bindAddress = null;
		if (!server().getHost().equals("*")) {
			try {
				bindAddress = InetAddress.getByName(server().getHost());
			} catch (UnknownHostException ex) {
				LOG.warn("Bind address is invalid, using all available IPs!", ex);
			}
		}
		
		try {
			_selectorThread.openServerSocket(bindAddress, server().getPort());
			_selectorThread.start();
			LOG.info("Now listening on {}:{}", server().getHost(), server().getPort());
		} catch (IOException ex) {
			LOG.error("Failed to open server socket!", ex);
			System.exit(1);
		}
		
		if (server().enableUPnP()) {
			startTimedSection("UPnP");
			UPnPService.getInstance().load(server().getPort(), "L2J Game Server");
		}
		
		if (telnet().isEnabled()) {
			new Status(telnet().getPort(), telnet().getPassword()).start();
		} else {
			LOG.info("Telnet server is currently disabled.");
		}
		
		LOG.info("Maximum numbers of connected players {}.", server().getMaxOnlineUsers());
		LOG.info("Server {} loaded in {} seconds.", ServerNameDAO.getServer(hexId().getServerID()), MILLISECONDS.toSeconds(System.currentTimeMillis() - serverLoadStart));
	}
	
	public static void main(String[] args) throws Exception {
		final String datapackRoot = Util.parseArg(args, DATAPACK, true);
		if (datapackRoot != null) {
			server().setProperty("DatapackRoot", datapackRoot);
		}
		
		final String scriptRoot = Util.parseArg(args, SCRIPT, true);
		if (scriptRoot != null) {
			server().setProperty("ScriptRoot", scriptRoot);
		}
		
		final String geodata = Util.parseArg(args, GEODATA, true);
		if (geodata != null) {
			geodata().setProperty("GeoDataPath", geodata);
		}
		
		gameServer = new GameServer();
	}
	
	public long getUsedMemoryMB() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	}
	
	public SelectorThread<L2GameClient> getSelectorThread() {
		return _selectorThread;
	}
	
	public L2GamePacketHandler getL2GamePacketHandler() {
		return _gamePacketHandler;
	}
	
	public DeadLockDetector getDeadLockDetectorThread() {
		return _deadDetectThread;
	}
	
	public static long startTimedSection(String sectionName) {
		final var sBuilder = new StringBuilder("=[ " + sectionName + " ]");
		while (sBuilder.length() < 61) {
			sBuilder.insert(0, "-");
		}
		LOG.info(sBuilder.toString());
		
		return System.currentTimeMillis();
	}
	
	private static void endTimedSection(String sectionName, long startTime) {
		LOG.info("[ {} loaded in {}ms ]", sectionName, System.currentTimeMillis() - startTime);
	}
}
