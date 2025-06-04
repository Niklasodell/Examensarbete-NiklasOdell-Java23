## Disaster Recovery

Denna applikation använder en named Docker-volym (`postgres-data`) för att bevara databasen mellan omstarter.

### Backup
Skapa en säkerhetskopia med:

```bash
./scripts/backup.sh
