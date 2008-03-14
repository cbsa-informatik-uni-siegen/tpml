#!/bin/sh
#
# $Id: keywords.sh 606 2008-03-07 18:43:10Z fehler $
#
# Copyright (c) 2008 Christian Fehler
#

# core
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.core/source/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.core/source-impl/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.core/test/ -name '*.java'`

# graphics
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.graphics/source/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.graphics/source-impl/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.graphics/test/ -name '*.java'`

# ui
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.ui/source/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../de.unisiegen.tpml.ui/test/ -name '*.java'`

# dev core
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.core/source/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.core/source-impl/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.core/test/ -name '*.java'`

# dev graphics
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.graphics/source/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.graphics/source-impl/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.graphics/test/ -name '*.java'`

# dev ui
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.ui/source/ -name '*.java'`
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.dev.ui/test/ -name '*.java'`

# javacup
svn propset svn:keywords 'Author Date Id Rev' `find ../tpml.javacup/source/ -name '*.java'`
