package com.andromeda.game.oldmap;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


public final class MapLoader<T> {
  public MapLoader(TileMap<T> map, MapGenerator<T> generator) {
    this.map = map;
    this.generator = generator;
  }

  public <R extends TileRequest> ListenableFuture<List<Tile<T>>> submit(R request) {
    return requests.submit(new RequestWorker<>(request));
  }

  private final TileMap<T> map;
  private final MapGenerator<T> generator;

  private final ListeningExecutorService requests =
      MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

  private static final Logger log = LoggerFactory.getLogger(MapLoader.class);


  private final class RequestWorker<R extends TileRequest> implements Callable<List<Tile<T>>> {
    private RequestWorker(R request) {
      this.request = request;
    }

    @Override
    public List<Tile<T>> call() throws Exception {
      log.debug("serving request: " + map.getName() + request);

      final Set<Tile<T>> missing = new HashSet<>();
      final Set<Tile<T>> found = new HashSet<>();

      if (request instanceof SingleTileRequest)
        findInMap((SingleTileRequest) request, found, missing);
      else if (request instanceof RadiusTileRequest)
        findInMap((RadiusTileRequest) request, found, missing);
      else
        assert false : "unhandled descendant of TileRequest";

      if (!missing.isEmpty())
        tryLoad(missing, found);

      if (!missing.isEmpty())
        generator.getDependencyTree(request);

      //if (!missing.isEmpty())
      //  generators[request.layer].submit(request);

      // 1) Try to get the tile from the model.
      // 2) Try to update the model from the database
      // 3) Generate the needed tiles from scratch

      /* The strategy for generation is as follows:
       * (1) get dependencies for the specified request from the appropriate generator.
       *      This is the head of the dependency tree.
       * (2) get dependencies for the tail of the dependency tree from the appropriate generator.
       *      null => go to (4)
       *      non-null => append result to the tail of the tree and go to (2).
       * (4) for each node in dependency tree:
       *      a) expand dependency to (x, y, layer) tuples
       *      b) add tuples to Loading set
       *      c) attach references to tuples to node in dependency tree
       * (5) for each tuple in Loading set:
       *      a) present in map => remove from set
       *      b) load from database => remove from set
       * (6) for each node in dependency tree:
       *      a) for each attached tuple:
       *        not in Loading set => remove from node
       *      b) node has no attached tuples => remove from dependency tree
       * (7) convert dependency tree to request tree
       * (8) for node in request tree, from tail to head:
       *      a) submit request to appropriate generator
       */
      //return generator.toLayer(x, y, layer);
      return null;
    }

    private void findInMap(SingleTileRequest request,
                           Collection<Tile<T>> foundGoHere,
                           Collection<Tile<T>> missingGoHere) {
      // Try to find in cache
      final Tile<T> tile = map.tiles().at(request.x, request.y);
      if (tile.getMaxLayer() >= request.layer)
        foundGoHere.add(tile);
      else
        missingGoHere.add(tile);
    }

    private void findInMap(RadiusTileRequest request,
                           Collection<Tile<T>> foundGoHere,
                           Collection<Tile<T>> missingGoHere) {
      final Tiles<T> tiles = map.tiles().inRadius(request.x, request.y, request.radius);

      for (Tile<T> tile : tiles) {
        if (tile.getMaxLayer() >= request.layer)
          foundGoHere.add(tile);
        else
          missingGoHere.add(tile);
      }
    }

    private void tryLoad(final Collection<Tile<T>> missing, Collection<Tile<T>> foundGoHere) {
      for (Tile<T> tile : missing)
        if ((tile = tryLoad(tile)) != null)
          foundGoHere.add(tile);
    }

    private Tile<T> tryLoad(Tile<T> tile) {
      // Retrieve the specified tile from the database
      return null;
    }

    private final R request;
  }
}
