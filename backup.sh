#!/bin/bash

echo "🔄 Skapar backup av PostgreSQL-databasen..."

docker exec -t examensarbete-database pg_dump -U root bookwishlist > db-backup.sql

echo "✅ Backup sparad som 'db-backup.sql'"
