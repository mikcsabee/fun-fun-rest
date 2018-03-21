name := """fun-fun-rest"""
organization := "com.csaba"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.46"
libraryDependencies += "io.ebean" % "ebean-agent" % "11.10.1"
libraryDependencies += "com.h2database" % "h2" % "1.4.197" % "test"

scalaVersion := "2.12.4"

libraryDependencies += guice
