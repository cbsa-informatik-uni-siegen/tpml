#!/bin/sh
#
# $Id: tpml.sh 443 2006-10-16 13:09:10Z benny $
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

# determine the real path of the shell script (somewhat hacky)
if echo "$0" | grep "^/" > /dev/null; then
	REALPATH=`dirname $0`;
else
	REALPATH=`pwd`
fi

# determine the $XDG_DATA_HOME directory
test x"$XDG_DATA_HOME" != x"" || export XDG_DATA_HOME="$HOME/.local/share"

# install the mime types
mkdir -p "$XDG_DATA_HOME/mime/packages"
cat > "$XDG_DATA_HOME/mime/packages/tpml.xml" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<mime-info xmlns="http://www.freedesktop.org/standards/shared-mime-info">
  <mime-type type="text/x-tpml">
    <sub-class-of type="text/plain" />
    <comment>TPML source code</comment>
    <comment xml:lang="de">TPML-Quelltext</comment>
    <glob pattern="*.[Ll][01234]" />
  </mime-type>
</mime-info>
EOF
update-mime-database "$XDG_DATA_HOME/mime" > /dev/null

# install the .desktop file
mkdir -p "$XDG_DATA_HOME/applications"
cat > "$XDG_DATA_HOME/applications/tpml.desktop" <<EOF
[Desktop Entry]
Encoding=UTF-8
Version=1.0
Type=Application
NoDisplay=false
Name=TPML 1.0.1
Comment=TPML education tool
Comment[de]=TPML Lernwerkzeug
TryExec="$REALPATH/tpml.sh"
Exec="$REALPATH/tpml.sh" %F
MimeType=text/x-tpml
StartupNotify=false
Categories=Education;Development;Java;
EOF
update-desktop-database "$XDG_DATA_HOME/applications" > /dev/null

# execute the application
exec java -jar "$REALPATH/de.unisiegen.tpml.ui-1.0.1.jar" "$@"
