#!/bin/sh
### BEGIN INIT INFO
# Provides: jasmarty
# Required-Start: $syslog
# Required-Stop: $syslog
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: jasmarty server
# Description: jasmarty
### END INIT INFO

case "$1" in

start)
        echo "jasmarty wird gestartet"
        cd /data/jasmarty
        java -jar jasmarty-1.0-SNAPSHOT-runner.jar >> jasmarty.log 2>> jasmarty.log &
        ;;
stop)
        echo "jasmarty wird beendet"
        curl -X POST http://localhost:8080/wipf/stop
        ;;
*)
        echo "jasmarty"
        exit 1
        ;;
esac
exit 0
