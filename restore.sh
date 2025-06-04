#!/bin/bash

if [ ! -f db-backup.sql ]; then
  echo "❌ Ingen 'db-backup.sql' hittades i projektmappen."
  exit 1
fi

echo "⚠️ Återställer databasen från backup..."

# Kopiera backupfilen in i containern
docker cp db-backup.sql examensarbete-database:/db-backup.sql

# Kör restore
docker exec -i examensarbete-database psql -U root -d bookwishlist -f /db-backup.sql

echo "✅ Återställning klar!"
