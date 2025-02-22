package dev.hepno.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import dev.hepno.platinum_api.packet.Packet;
import dev.hepno.platinum_api.packet.PlayerFreeCoinPacket;
import dev.hepno.platinum_game_client.PlatinumGameClientApplication;
import dev.hepno.platinum_game_client.UdpClient;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private VisTable table;

    private PlatinumGameClientApplication platinumGameClientApplication;

    @Override
    public void create() {
        platinumGameClientApplication = new PlatinumGameClientApplication();
        try {
            platinumGameClientApplication.run(new String[]{});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        batch = new SpriteBatch();

        VisUI.load();

        table = new VisTable();
        VisTextButton button = new VisTextButton("send a packet");
        table.setFillParent(true);
        button.setPosition(260, 240);

        button.addListener(new ClickListener() {
           @Override
            public void clicked(InputEvent event, float x, float y) {
               try {
                   platinumGameClientApplication.getClient().write(new PlayerFreeCoinPacket(11, 1, 5));
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        table.addActor(button);

        Stage stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        table.draw(batch, 1);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        platinumGameClientApplication.getClient().shutdown();
    }
}
