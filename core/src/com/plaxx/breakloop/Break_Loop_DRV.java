package com.plaxx.breakloop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;

public class Break_Loop_DRV extends ApplicationAdapter{
	private static boolean init = true;
	private static final String lvlFile = "lvl99.txt";
	private static final int TILE_WIDTH = 48;
	private static final int TILE_HEIGHT = 48;
	private static int frameWidth = 480;
	private static int frameHeight = 624;
	private static int gridWidth;
	private static int gridHeight;

	private Tile[][] grid;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private TextureAtlas imageAtlas;
	private Stage stage;

	private TextureRegion endp;
	private TextureRegion line;
	private TextureRegion corn;
	private TextureRegion bone;
	private TextureRegion cros;
	private TextureRegion ovly_r;
	private TextureRegion ovly_g;
	private TextureRegion ovly_b;

	@Override
	public void create(){
		batch = new SpriteBatch();
		imageAtlas = new TextureAtlas("BreakLoopAtlas.pack");
		
		endp = imageAtlas.findRegion("endp");
		line = imageAtlas.findRegion("line");
		corn = imageAtlas.findRegion("corn");
		bone = imageAtlas.findRegion("bone");
		cros = imageAtlas.findRegion("cros");
		ovly_r = imageAtlas.findRegion("ovly_r");
		ovly_g = imageAtlas.findRegion("ovly_g");
		ovly_b = imageAtlas.findRegion("ovly_b");


		grid = Loader.loadGrid(lvlFile);
		if(grid == null){
			grid = new Tile[0][0];

			camera = new OrthographicCamera();
			camera.setToOrtho(false, frameWidth, frameHeight);
			return;
		}

		gridHeight = grid.length;
		gridWidth = grid[0].length;
		frameWidth = gridWidth * TILE_WIDTH;
		frameHeight = gridHeight * TILE_HEIGHT;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, frameWidth, frameHeight);

		Solver.init(grid);
		Solver.scramble(grid);
		float delay = 1;

		Timer.schedule(new Timer.Task(){
			@Override
			public void run(){
				init = false;
			}
		}, delay);
	}

	@Override
	public void render(){
		Gdx.gl.glClearColor(0.286f, 0.298f, 0.502f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		if(!init)
			Solver.solveStep(grid);

		for(int j = 0; j < gridHeight; j++){
			for(int i = 0; i < gridWidth; i++){
				float x = i * TILE_WIDTH;
				float y = j * TILE_HEIGHT;

				Tile t = grid[j][i];
				//t.rotateCW(MathUtils.random(0, 5));

				switch(t.getType()){
					case endp:
						batch.draw(endp, x, y, TILE_WIDTH / 2, TILE_HEIGHT / 2, TILE_WIDTH, TILE_HEIGHT, 1, 1, -t.getDegrees());
						break;
					case line:
						batch.draw(line, x, y, TILE_WIDTH / 2, TILE_HEIGHT / 2, TILE_WIDTH, TILE_HEIGHT, 1, 1, -t.getDegrees());
						break;
					case corn:
						batch.draw(corn, x, y, TILE_WIDTH / 2, TILE_HEIGHT / 2, TILE_WIDTH, TILE_HEIGHT, 1, 1, -t.getDegrees());
						break;
					case bone:
						batch.draw(bone, x, y, TILE_WIDTH / 2, TILE_HEIGHT / 2, TILE_WIDTH, TILE_HEIGHT, 1, 1, -t.getDegrees());
						break;
					case cros:
						batch.draw(cros, x, y, TILE_WIDTH / 2, TILE_HEIGHT / 2, TILE_WIDTH, TILE_HEIGHT, 1, 1, -t.getDegrees());
						break;
				}

				if(t.isLocked())
					batch.draw(ovly_g, x, y);
				//
				//if(t.isMarked())
				//	batch.draw(ovly_g, x, y);
			}
		}
		batch.end();
	}

	@Override
	public void dispose(){
		batch.dispose();
		imageAtlas.dispose();
	}
}
