#!/bin/bash
kill $(ps fax | grep jasmarty-dev.jar | grep -v grep | awk '{print $1;}')