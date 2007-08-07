@echo off
echo Windows Registry Editor Version 5.00 >test.reg
echo. >>test.reg
echo [HKEY_CLASSES_ROOT\Applications\tpml.exe]>>test.reg
echo. >>test.reg
echo [HKEY_CLASSES_ROOT\Applications\tpml.exe\shell]>>test.reg
echo. >>test.reg
echo [HKEY_CLASSES_ROOT\Applications\tpml.exe\shell\open]>>test.reg
::echo. >>test.reg
::echo [HKEY_CLASSES_ROOT\Applications\tpml.exe\shell\open\command]>>test.reg
::echo ^@="\"%cd%\tpml.exe\" \"%%1\"">>test.reg

regedit /s test.reg
del test.reg
regedit /s regfiles.reg

::reg add HKEY_CURRENT_USER\Software\Classes\Applications\tpml.exe\shell\open\command /ve /t REG_SZ /d ""%cd%\tpml.exe" ""%%1""""
set key="%cd%\tpml.exe %%1"
::echo %key%
::pause
reg add HKEY_CURRENT_USER\Software\Classes\Applications\tpml.exe\shell\open\command /ve /t REG_SZ /d %key%
::pause

:end
set key=