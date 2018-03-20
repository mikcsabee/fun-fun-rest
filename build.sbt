name := """fun-fun-rest"""
organization := "com.csaba"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.46"

scalaVersion := "2.12.4"

libraryDependencies += guice
