#!/bin/sh
#
# $Id$
#
# Startscript for the TPML application on Linux/Unix.
#

# Check if a JRE is available
if ! type java >/dev/null 2>&1; then
	cat >&2 <<EOF
No suitable JRE found. Please download and install a JRE and make sure the
java binary is in the PATH.
EOF
	exit 1
fi

exec java -jar de.unisiegen.tpml.ui-@de.unisiegen.tpml.ui.version@.jar
