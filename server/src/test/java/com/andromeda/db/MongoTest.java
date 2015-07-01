package com.andromeda.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MongoTest {
  @Before
  public void connect() {
    client = new MongoClient();
    db = client.getDatabase("andromeda");
  }

  @After
  public void tearDown() {
    client.close();
  }

  @Test
  public void createCollection() {
    MongoCollection<Document> players = db.getCollection("players");
    assertEquals(0, players.count());

    players.insertOne(
        new Document(
            "player",
            new Document()
                .append("name", "Matt")
        )
    );

    assertEquals(1, players.count());
  }

  private MongoClient client;
  private MongoDatabase db;
}
