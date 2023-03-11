package com.aramirezochoa.earm.state.level.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leboheme on 24/08/2014.
 */
public class LevelManagerTest {

    public static void main(String args[]) {
        WorldDescription world = new WorldDescription();
        List<LevelDescription> levels = new ArrayList<LevelDescription>();
        for (int i = 1; i <= 25; i++) {
            LevelDescription level = new LevelDescription();
            level.setNumber(i);
            level.setEnabled(i % 2 == 0);
            levels.add(level);
        }
        world.setLevels(levels);

        Map<WorldType, WorldDescription> worlds = new HashMap<WorldType, WorldDescription>();
        worlds.put(WorldType.WOODS, world);

        Json json = new Json(JsonWriter.OutputType.json);
//        System.out.println(json.prettyPrint(worlds));
        String text = json.toJson(worlds);

        FileHandle file = Gdx.files.internal("/data/stages/test.json");
        file.writeString(text, false);

//        Map<WorldType, WorldDescription> read = json.fromJson(HashMap.class, text);
    }

}
