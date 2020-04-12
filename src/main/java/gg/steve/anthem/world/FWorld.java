package gg.steve.anthem.world;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class FWorld implements World {
    private World world;
    private int plotSize;
    private int factionArea;


    public FWorld(World world, int plotSize, int factionArea) {
        this.world = world;
        this.plotSize = plotSize;
        this.factionArea = factionArea;
    }

    public boolean inFactionLand(Location location) {
        if (!location.getWorld().equals(world)) {
            return false;
        }
        int x = location.getBlockX();
        int z = location.getBlockZ();
        return x >= -(factionArea + 1) && x <= factionArea && z >= -(factionArea + 1) && z <= factionArea;
    }

    public List<Block> getFactionArea() {
        List<Block> blocks = new ArrayList<>();
        for (int x = -(factionArea + 1); x <= factionArea; x++) {
            for (int z = -(factionArea + 1); z <= factionArea; z++) {
                for (int y = 1; y <= 256; y++) {
                    if (world.getBlockAt(x,y,z).getType().equals(Material.AIR)) continue;
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return this.world.getBlockAt(x, y, z);
    }

    @Override
    public Block getBlockAt(Location location) {
        return this.world.getBlockAt(location);
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return this.world.getHighestBlockYAt(x, z);
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return this.world.getHighestBlockYAt(location);
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return this.world.getHighestBlockAt(x, z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return this.world.getHighestBlockAt(location);
    }

    @Override
    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        return this.world.getHighestBlockYAt(x, z, heightMap);
    }

    @Override
    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        return this.world.getHighestBlockYAt(location, heightMap);
    }

    @Override
    public Block getHighestBlockAt(int x, int z, HeightMap heightMap) {
        return this.world.getHighestBlockAt(x, z, heightMap);
    }

    @Override
    public Block getHighestBlockAt(Location location, HeightMap heightMap) {
        return this.world.getHighestBlockAt(location, heightMap);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return this.world.getChunkAt(x, z);
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return this.world.getChunkAt(location);
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return this.world.getChunkAt(block);
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return this.world.isChunkLoaded(chunk);
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return this.world.getLoadedChunks();
    }

    @Override
    public void loadChunk(Chunk chunk) {
        this.world.loadChunk(chunk);
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return this.world.isChunkLoaded(x, z);
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return this.world.isChunkGenerated(x, z);
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return this.world.isChunkInUse(x, z);
    }

    @Override
    public void loadChunk(int x, int z) {
        this.world.loadChunk(x, z);
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        return this.world.loadChunk(x, z, generate);
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return this.world.unloadChunk(chunk);
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return this.world.unloadChunk(x, z);
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return this.world.unloadChunk(x, z, save);
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return this.world.unloadChunkRequest(x, z);
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        return this.world.regenerateChunk(x, z);
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        return this.world.refreshChunk(x, z);
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return this.world.isChunkForceLoaded(x, z);
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        this.world.setChunkForceLoaded(x, z, forced);
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        return this.world.getForceLoadedChunks();
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        return this.world.addPluginChunkTicket(x, z, plugin);
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        return this.world.removePluginChunkTicket(x, z, plugin);
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {
        this.world.removePluginChunkTickets(plugin);
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        return this.world.getPluginChunkTickets(x, z);
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return this.world.getPluginChunkTickets();
    }

    @Override
    public Item dropItem(Location location, ItemStack item) {
        return this.world.dropItem(location, item);
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        return this.world.dropItem(location, item);
    }

    @Override
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
        return this.world.spawnArrow(location, direction, speed, spread);
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location location, Vector direction, float speed, float spread, Class<T> clazz) {
        return this.world.spawnArrow(location, direction, speed, spread, clazz);
    }

    @Override
    public boolean generateTree(Location location, TreeType type) {
        return this.world.generateTree(location, type);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        return this.world.generateTree(loc, type, delegate);
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type) {
        return this.world.spawnEntity(loc, type);
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        return this.world.strikeLightning(loc);
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        return strikeLightningEffect(loc);
    }

    @Override
    public List<Entity> getEntities() {
        return this.world.getEntities();
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        return this.world.getLivingEntities();
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return this.world.getEntitiesByClass(classes);
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        return this.world.getEntitiesByClass(cls);
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        return this.world.getEntitiesByClasses(classes);
    }

    @Override
    public List<Player> getPlayers() {
        return this.world.getPlayers();
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        return this.world.getNearbyEntities(location, x, y, z);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<Entity> filter) {
        return this.world.getNearbyEntities(location, x, y, z, filter);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return this.world.getNearbyEntities(boundingBox);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        return this.world.getNearbyEntities(boundingBox, filter);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        return this.world.rayTraceEntities(start, direction, maxDistance);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        return this.world.rayTraceEntities(start, direction, maxDistance, raySize);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<Entity> filter) {
        return this.world.rayTraceEntities(start, direction, maxDistance, filter);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize, Predicate<Entity> filter) {
        return this.world.rayTraceEntities(start, direction, maxDistance, raySize, filter);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        return this.world.rayTraceBlocks(start, direction, maxDistance);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return this.world.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        return this.world.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks);
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<Entity> filter) {
        return this.world.rayTrace(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, raySize, filter);
    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public UUID getUID() {
        return this.world.getUID();
    }

    @Override
    public Location getSpawnLocation() {
        return this.world.getSpawnLocation();
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        return this.world.setSpawnLocation(location);
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return this.world.setSpawnLocation(x, y, z);
    }

    @Override
    public long getTime() {
        return this.world.getTime();
    }

    @Override
    public void setTime(long time) {
        this.world.setTime(time);
    }

    @Override
    public long getFullTime() {
        return this.world.getFullTime();
    }

    @Override
    public void setFullTime(long time) {
        this.world.setFullTime(time);
    }

    @Override
    public boolean hasStorm() {
        return this.world.hasStorm();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        this.world.setStorm(hasStorm);
    }

    @Override
    public int getWeatherDuration() {
        return this.world.getWeatherDuration();
    }

    @Override
    public void setWeatherDuration(int duration) {
        this.world.setWeatherDuration(duration);
    }

    @Override
    public boolean isThundering() {
        return this.world.isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        this.world.setThundering(thundering);
    }

    @Override
    public int getThunderDuration() {
        return this.world.getThunderDuration();
    }

    @Override
    public void setThunderDuration(int duration) {
        this.world.setThunderDuration(duration);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return this.world.createExplosion(x, y, z, power);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return this.world.createExplosion(x, y, z, power, setFire);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return this.world.createExplosion(x, y, z, power, setFire, breakBlocks);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source) {
        return this.world.createExplosion(x, y, z, power, setFire, breakBlocks, source);
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return this.world.createExplosion(loc, power);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return this.world.createExplosion(loc, power, setFire);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        return this.world.createExplosion(loc, power, setFire, breakBlocks);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        return this.world.createExplosion(loc, power, setFire, breakBlocks, source);
    }

    @Override
    public Environment getEnvironment() {
        return this.world.getEnvironment();
    }

    @Override
    public long getSeed() {
        return this.world.getSeed();
    }

    @Override
    public boolean getPVP() {
        return this.world.getPVP();
    }

    @Override
    public void setPVP(boolean pvp) {
        this.world.setPVP(pvp);
    }

    @Override
    public ChunkGenerator getGenerator() {
        return this.world.getGenerator();
    }

    @Override
    public void save() {
        this.world.save();
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return this.world.getPopulators();
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return this.world.spawn(location, clazz);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return this.world.spawn(location, clazz, function);
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        return this.world.spawnFallingBlock(location, data);
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        return this.world.spawnFallingBlock(location, data);
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException {
        return this.world.spawnFallingBlock(location, material, data);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        this.world.playEffect(location, effect, data);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        this.world.playEffect(location, effect, data, radius);
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {
        this.world.playEffect(location, effect, data);
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data, int radius) {
        this.world.playEffect(location, effect, data, radius);
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        return getEmptyChunkSnapshot(x, z, includeBiome, includeBiomeTemp);
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        this.world.setSpawnFlags(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return this.world.getAllowAnimals();
    }

    @Override
    public boolean getAllowMonsters() {
        return this.world.getAllowMonsters();
    }

    @Override
    public Biome getBiome(int x, int z) {
        return this.world.getBiome(x, z);
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return this.world.getBiome(x, y, z);
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        this.world.setBiome(x, z, bio);
    }

    @Override
    public void setBiome(int x, int y, int z, Biome bio) {
        this.world.setBiome(x, y, z, bio);
    }

    @Override
    public double getTemperature(int x, int z) {
        return this.world.getTemperature(x, z);
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        return this.world.getTemperature(x, y, z);
    }

    @Override
    public double getHumidity(int x, int z) {
        return this.world.getHumidity(x, z);
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return this.world.getHumidity(x, y, z);
    }

    @Override
    public int getMaxHeight() {
        return this.world.getMaxHeight();
    }

    @Override
    public int getSeaLevel() {
        return this.world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return this.world.getKeepSpawnInMemory();
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        this.world.setKeepSpawnInMemory(keepLoaded);
    }

    @Override
    public boolean isAutoSave() {
        return this.world.isAutoSave();
    }

    @Override
    public void setAutoSave(boolean value) {
        this.world.setAutoSave(value);
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.world.setDifficulty(difficulty);
    }

    @Override
    public Difficulty getDifficulty() {
        return this.world.getDifficulty();
    }

    @Override
    public File getWorldFolder() {
        return this.world.getWorldFolder();
    }

    @Override
    public WorldType getWorldType() {
        return this.world.getWorldType();
    }

    @Override
    public boolean canGenerateStructures() {
        return this.world.canGenerateStructures();
    }

    @Override
    public boolean isHardcore() {
        return this.world.isHardcore();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        this.world.setHardcore(hardcore);
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return this.world.getTicksPerAnimalSpawns();
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        this.world.setTicksPerAnimalSpawns(ticksPerAnimalSpawns);
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return this.world.getTicksPerMonsterSpawns();
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        this.world.setTicksPerAnimalSpawns(ticksPerMonsterSpawns);
    }

    @Override
    public long getTicksPerWaterSpawns() {
        return this.world.getTicksPerWaterSpawns();
    }

    @Override
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        this.world.setTicksPerWaterSpawns(ticksPerWaterSpawns);
    }

    @Override
    public long getTicksPerAmbientSpawns() {
        return this.world.getTicksPerAmbientSpawns();
    }

    @Override
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        this.world.setTicksPerAmbientSpawns(ticksPerAmbientSpawns);
    }

    @Override
    public int getMonsterSpawnLimit() {
        return this.world.getMonsterSpawnLimit();
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        this.world.setMonsterSpawnLimit(0);
    }

    @Override
    public int getAnimalSpawnLimit() {
        return this.world.getAnimalSpawnLimit();
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        this.world.setAnimalSpawnLimit(limit);
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return this.world.getWaterAnimalSpawnLimit();
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        this.world.setWaterAnimalSpawnLimit(limit);
    }

    @Override
    public int getAmbientSpawnLimit() {
        return this.world.getAmbientSpawnLimit();
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
        this.world.setAmbientSpawnLimit(limit);
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        this.world.playSound(location, sound, volume, pitch);
    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        this.world.playSound(location, sound, volume, pitch);
    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        this.world.playSound(location, sound, category, volume, pitch);
    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        this.world.playSound(location, sound, category, volume, pitch);
    }

    @Override
    public String[] getGameRules() {
        return this.world.getGameRules();
    }

    @Override
    public String getGameRuleValue(String rule) {
        return this.world.getGameRuleValue(rule);
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        return this.world.setGameRuleValue(rule, value);
    }

    @Override
    public boolean isGameRule(String rule) {
        return this.world.isGameRule(rule);
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        return this.world.getGameRuleValue(rule);
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        return this.world.getGameRuleDefault(rule);
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        return this.world.setGameRule(rule, newValue);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.world.getWorldBorder();
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        this.world.spawnParticle(particle, location, count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        this.world.spawnParticle(particle, x, y, z, count);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        this.world.spawnParticle(particle, location, count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        this.world.spawnParticle(particle, x, y, z, count, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        this.world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        this.world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored) {
        return this.world.locateNearestStructure(origin, structureType, radius, findUnexplored);
    }

    @Override
    public int getViewDistance() {
        return this.world.getViewDistance();
    }

    @Override
    public Spigot spigot() {
        return this.world.spigot();
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        return this.world.locateNearestRaid(location, radius);
    }

    @Override
    public List<Raid> getRaids() {
        return this.world.getRaids();
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        return this.world.getEnderDragonBattle();
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.world.setMetadata(metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.world.getMetadata(metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.world.hasMetadata(metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.world.removeMetadata(metadataKey, owningPlugin);
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        this.world.sendPluginMessage(source, channel, message);
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return this.world.getListeningPluginChannels();
    }
}
