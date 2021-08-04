package net.spirangle.minerva.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameBase extends Game {

    public enum AssetFileType {
        TEXTURE,
        BITMAP_FONT,
        SOUND,
        MUSIC,
        I18BUNDLE;
    }

    private static class AssetFile {
        final int id;
        final String name;
        final String path;
        final AssetFileType type;

        AssetFile(int id,String name,String path,AssetFileType type) {
            this.id = id;
            this.name = name;
            this.path = path;
            this.type = type;
        }
    }

    public static I18NBundle str;

    private static GameBase instance = null;
    private static float fpsTimer = 0.0f;

    private AssetManager assets;
    private final List<AssetFile> assetFiles;
    private final Map<Integer,Texture> textures;
    private final Map<Integer,BitmapFont> bitmapFonts;
    private final Map<Integer,Sound> sounds;
    private final Map<Integer,Music> musicById;
    private final Map<String,Music> musicByName;
    private Music bgmusic;

    public static GameBase getInstance() {
        return instance;
    }

    public static void sleepFPS(float fps,float delta) {
        delta += fpsTimer;
        float rate = 1.0f/fps;
        float sleep = rate-delta;
        if(sleep>0.0f) {
            fpsTimer = 0.0f;
        } else {
            fpsTimer = delta-rate;
            sleep = rate*0.5f;
        }
        try {
//Gdx.app.log("BasicGame.sleepFPS","rate: "+rate+", delta: "+delta+", sleep: "+sleep+", fpsTimer: "+fpsTimer);
            Thread.sleep((long)(sleep*1000.f));
        } catch(InterruptedException ex) {
            Gdx.app.log("BasicGame.sleepFPS","Exception: "+ex.getMessage()+"\n"+getStackTraceString(ex)+"\n");
        }
    }

    public GameBase() {
        instance = this;
        assetFiles = new ArrayList<>();
        bitmapFonts = new HashMap<>();
        textures = new HashMap<>();
        sounds = new HashMap<>();
        musicById = new HashMap<>();
        musicByName = new HashMap<>();
        bgmusic = null;
    }

    @Override
    public void create() {
        assets = new AssetManager();
        BitmapFontParameter fontParameter = new BitmapFontParameter();
        fontParameter.flip = true;
        for(AssetFile af : assetFiles) {
            switch(af.type) {
                case TEXTURE:
                    assets.load(af.path,Texture.class);
                    break;
                case BITMAP_FONT:
                    assets.load(af.path,BitmapFont.class,fontParameter);
                    break;
                case SOUND:
                    assets.load(af.path,Sound.class);
                    break;
                case MUSIC:
                    assets.load(af.path,Music.class);
                    break;
                case I18BUNDLE:
                    assets.load(af.path,I18NBundle.class);
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        stopMusic();

        Screen s = getScreen();
        s.dispose();

        super.dispose();
        assets.dispose();
    }

    public void loadingAssetsCompleted() {
        int i;
        String[] files;

        for(AssetFile af : assetFiles) {
            switch(af.type) {
                case TEXTURE:
                    textures.put(af.id,assets.get(af.path,Texture.class));
                    break;
                case BITMAP_FONT:
                    bitmapFonts.put(af.id,assets.get(af.path,BitmapFont.class));
                    break;
                case SOUND:
                    sounds.put(af.id,assets.get(af.path,Sound.class));
                    break;
                case MUSIC:
                    if(af.id>=0) musicById.put(af.id,assets.get(af.path,Music.class));
                    if(af.name!=null) musicByName.put(af.name,assets.get(af.path,Music.class));
                    break;
                case I18BUNDLE:
                    str = assets.get(af.path,I18NBundle.class);
                    break;
            }
        }
    }

    public void addAsset(int id,String path,AssetFileType type) {
        addAsset(id,null,path,type);
    }

    public void addAsset(String name,String path,AssetFileType type) {
        addAsset(-1,name,path,type);
    }

    public void addAsset(int id,String name,String path,AssetFileType type) {
        assetFiles.add(new AssetFile(id,name,path,type));
    }

    public abstract String getAppName();

    public AssetManager getAssetManager() {
        return assets;
    }

    public BitmapFont getFont(int id) {
        return bitmapFonts.get(id);
    }

    public Texture getTexture(int id) {
        return textures.get(id);
    }

    public abstract Texture getGUITexture();

    public long playSound(int id) {
        Sound sound = sounds.get(id);
        return sound!=null? sound.play() : -1;
    }

    public long playSound(int id,float v) {
        Sound sound = sounds.get(id);
        return sound!=null? sound.play(v) : -1;
    }

    public long playSound(int id,float v,float p,float a) {
        Sound sound = sounds.get(id);
        return sound!=null? sound.play(v,p,a) : -1;
    }

    public long loopSound(int id) {
        Sound sound = sounds.get(id);
        return sound!=null? sound.loop() : -1;
    }

    public long loopSound(int id,float v) {
        Sound sound = sounds.get(id);
        return sound!=null? sound.loop(v) : -1;
    }

    public long loopSound(int id,float v,float p,float a) {
        Sound sound = sounds.get(id);
        return sound!=null? sound.loop(v,p,a) : -1;
    }

    public void stopSound(int id) {
        Sound sound = sounds.get(id);
        if(sound!=null) sound.stop();
    }

    public void stopSound(int id,long n) {
        Sound sound = sounds.get(id);
        if(sound!=null) sound.stop(n);
    }

    public void playMusic(int id) {
        playMusic(musicById.get(id));
    }

    public void playMusic(String name) {
        playMusic(name!=null? musicByName.get(name) : null);
    }

    private void playMusic(Music music) {
        if(bgmusic!=null) {
            if(bgmusic==music) return;
            bgmusic.stop();
        }
        bgmusic = music;
        if(music!=null) {
            music.play();
            music.setLooping(true);
        }
    }

    public void stopMusic() {
        if(bgmusic==null) return;
        bgmusic.stop();
        bgmusic = null;
    }

    public static String getStackTraceString(Throwable e) {
        return getStackTraceString(e,"");
    }

    private static String getStackTraceString(Throwable e,String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString());
        sb.append("\n");

        StackTraceElement[] stack = e.getStackTrace();
        if(stack!=null) {
            for(StackTraceElement stackTraceElement : stack) {
                sb.append(indent);
                sb.append("\tat ");
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
        }

        Throwable[] suppressedExceptions = e.getSuppressed();
        // Print suppressed exceptions indented one level deeper.
        if(suppressedExceptions!=null) {
            for(Throwable throwable : suppressedExceptions) {
                sb.append(indent);
                sb.append("\tSuppressed: ");
                sb.append(getStackTraceString(throwable,indent+"\t"));
            }
        }

        Throwable cause = e.getCause();
        if(cause!=null) {
            sb.append(indent);
            sb.append("Caused by: ");
            sb.append(getStackTraceString(cause,indent));
        }

        return sb.toString();
    }
}
