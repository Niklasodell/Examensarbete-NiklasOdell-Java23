# 🔄 Disaster Recovery - Examensarbete

Detta projekt innehåller stöd för säkerhetskopiering (backup) och återställning (restore) av PostgreSQL-databasen som körs i Docker.

## 🧰 Innehåll

Det finns två script:

- `backup.sh`: Skapar en backup av databasen.
- `restore.sh`: Återställer databasen från den senast sparade backup-filen (`db-backup.sql`).

---

## 🚀 Hur du använder

### 1. Skapa en backup

```bash
bash backup.sh