/*
 * Item Editor: Modify items with ease in Minecraft servers.
 * Copyright (C) 2021 Dreta
 *
 * Item Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Item Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Item Editor. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dreta.itemeditor.utils;

import dev.dreta.itemeditor.ItemEditor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

/**
 * This is a utility class that allows you to easily
 * create custom configuration files and access them.
 */
public class Configuration {
    @Getter
    @Setter
    private File file;
    @Getter
    @Setter
    private FileConfiguration config;

    /**
     * Creates a new Configuration and loads it with a resource.
     *
     * @param resourceName The resource name to load from
     * @return The created configuration
     */
    public static Configuration loadConfiguration(String resourceName) {
        Configuration config = new Configuration();
        config.load(resourceName);
        return config;
    }

    /**
     * Save the configuration.
     */
    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reload the configuration.
     */
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Read string from an InputStream.
     *
     * @param stream The stream to read from
     * @return The contents of the stream.
     */
    public String readInputStream(InputStream stream) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String resp;
        try {
            while ((resp = reader.readLine()) != null) {
                builder.append(resp);
                builder.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * Load this configuration from a file.
     *
     * @param from The file to load from
     */
    public void load(String from) {
        load(from, ItemEditor.class, ItemEditor.inst.getDataFolder());
    }

    /**
     * Load this configuration from a file.
     *
     * @param from       The file to load from
     * @param clazz      The class to discover to resource in
     * @param dataFolder The data folder to put the configuration to
     */
    public void load(String from, Class<?> clazz, File dataFolder) {
        try {
            if (!dataFolder.exists()) dataFolder.mkdirs();
            this.file = new File(dataFolder, from);
            if (!this.file.exists()) {
                if (!this.file.createNewFile()) throw new IOException("File creation failed");
                BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
                writer.write(readInputStream(clazz.getResourceAsStream("/" + from)));
                writer.flush();
                writer.close();
            }
            this.config = YamlConfiguration.loadConfiguration(this.file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

