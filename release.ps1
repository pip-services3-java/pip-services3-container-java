#!/usr/bin/env pwsh

Set-StrictMode -Version latest
$ErrorActionPreference = "Stop"

$component = Get-Content -Path "component.json" | ConvertFrom-Json
$version = ([xml](Get-Content -Path "pom.xml")).project.version

# Verify versions in component.json and pom.xml
if ($component.version -ne $version) {
    throw "Versions in component.json and pom.xml do not match"
}

# Create ~/.m2/settings.xml if not exists
if (!(Test-Path "~/.m2/settings.xml")) {
    $m2SetingsContent = @"
<?xml version="1.0" encoding="UTF-8"?>
<settings>
   <servers>
      <server>
         <id>ossrh</id>
         <username>$($env:M2_USER)</username>
         <password>$($env:M2_PASS)</password>
      </server>
      <server>
         <id>sonatype-nexus-snapshots</id>
         <username>$($env:M2_USER)</username>
         <password>$($env:M2_PASS)</password>
      </server>
      <server>
         <id>nexus-releases</id>
         <username>$($env:M2_USER)</username>
         <password>$($env:M2_PASS)</password>
      </server>
   </servers>
   <profiles>
      <profile>
         <id>ossrh</id>
         <activation>
            <activeByDefault>true</activeByDefault>
         </activation>
         <properties>
            <gpg.keyname>$($env:GPG_KEYNAME)</gpg.keyname>
            <gpg.executable>gpg</gpg.executable>
            <gpg.passphrase>$($env:GPG_PASSPHRASE)</gpg.passphrase>
         </properties>
      </profile>
   </profiles>
</settings>
"@

    if (!(Test-Path "~/.m2")) {
        New-Item -Path "~/.m2" -ItemType "directory"
    }

    Set-Content -Path "~/.m2/settings.xml" -Value $m2SetingsContent
}

# Release package
mvn clean deploy

# Verify release result
if ($LastExitCode -ne 0) {
    Write-Error "Release failed. Watch logs above."
}
