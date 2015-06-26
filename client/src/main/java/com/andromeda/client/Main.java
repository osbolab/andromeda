package com.andromeda.client;

import java.net.InetSocketAddress;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;

public class Main {
  public static void main(String[] args) {
    final InetSocketAddress remote = new InetSocketAddress("localhost", 9999);

    final ActorSystem system = ActorSystem.create("andromeda-client");
    final Inbox inbox = Inbox.create(system);

    final ActorRef handler = system.actorOf(Props.create(SimpleHandler.class), "message-handler");
    final ActorRef client = system.actorOf(Client.to(remote, handler), "client");

    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();

    system.shutdown();
  }
}
