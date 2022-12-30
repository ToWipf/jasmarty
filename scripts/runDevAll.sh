#!/bin/bash
echo "starten"
./runDevBackend.sh &
./runDevProxyFrontend.sh &
echo "run"
