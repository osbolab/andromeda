package com.andromeda.game.map;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


public final class MapController<T> {
  public MapController(TileMap<T> map, MapGenerator<T> generator) {
    this.map = map;
    this.generate = generator;
  }

  /**
   * Submit a request to retrieve, load, or generate the tile at {@code (x, y)} to the given detail
   * layer.
   * <p>
   * The returned future will complete when the given {@link TileMap} contains the tile and all of
   * its dependencies.
   * <p>
   * E.g. if the tile {@code (x, y)} is requested at {@code layer 1} from an empty map, the returned
   * future will complete after all of the generators in {@code initial <-- layer 0 <-- layer 1}
   * have completed up to that tile.
   */
  public ListenableFuture<Tile<T>> requestTile(int x, int y, int layer) {
    return requests.submit(new RequestWorker(x, y, layer));
  }

  private final TileMap<T> map;
  private final MapGenerator<T> generate;
  private final ListeningExecutorService requests =
      MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

  private static final Logger log = LoggerFactory.getLogger(MapController.class);


  private final class RequestWorker implements Callable<Tile<T>> {
    private RequestWorker(int x, int y, int layer) {
      this.x = x;
      this.y = y;
      this.layer = layer;
    }

    @Override
    public Tile<T> call() throws Exception {
      log.debug("serving request: %s(%d, %d) at layer %d", map.getName(), x, y, layer);

      // 1) Try to get the tile from the model.
      Tile<T> tile = map.tiles().at(x, y);
      if (tile.getMaxLayer() >= layer)
        return tile;

      // 2) Try to update the model from the database


      /* 3) Generate a new tile by constructing a chain of dependent generators and applying them
       * to the map. Pipeline generators for dependent layers so that partially-complete
       * layers can begin to satisfy the next layer while continuing to progress.
       */

      return generate.toLayer(x, y, layer);
    }

    private final int x;
    private final int y;
    private final int layer;
  }
}
