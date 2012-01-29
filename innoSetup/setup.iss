#define MyAppName "S Instant Messenger"
#define MyAppVersion "0.0.7"
#define MyAppPublisher "Flo"
#define MyAppURL "https://github.com/anrub/SIM"
#define LaunchProgram "Starte S Instant Messenger nach der Installation"
#define DesktopIcon "Verknüpfung auf dem Desktop"
#define CreateDesktopIcon "Wollen Sie eine Verknüpfung auf dem Desktop erstellen?"

[Setup]
AppId={{F3E30478-2D70-4CBC-AB4F-0B7A0A4D44AB}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DefaultGroupName={#MyAppName}
OutputBaseFilename=setup
Compression=lzma
SolidCompression=yes

[Languages]
Name: "german"; MessagesFile: "compiler:Languages\German.isl"

[Files]
Source: "../sim/target/sim-{#MyAppVersion}-SNAPSHOT-jar-with-dependencies.jar"; DestDir: "{app}"; DestName: "sim.jar"; Flags: ignoreversion
Source: "icon.ico"; DestDir: "{app}"; DestName: "icon.ico"; Flags: ignoreversion

[Tasks]
Name: "desktopicon"; Description: "{#CreateDesktopIcon}"; GroupDescription: "{#DesktopIcon}"

[Icons]
Name: "{group}\SIM - S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"; IconFilename: "{app}/icon.ico"
Name: "{userstartup}\SIM - S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"; IconFilename: "{app}/icon.ico"
Name: "{userdesktop}\SIM - S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"; IconFilename: "{app}/icon.ico"; Tasks: desktopicon

[Code]
var
  Page: TInputDirWizardPage;
  Database: String;

procedure InitializeWizard;
begin
  
  Page := CreateInputDirPage(wpWelcome,
    'Datenbankpfad', 'Bitte Ordner der Datenbank auswählen',
    'Bitte den Ordner der Datenbank auswählen!', False, 'Neuer Ordner');

  Page.Add('');             
end;

function GetDatabase(Param: String): String;
begin
  { Return the selected DataDir }
  Result := Page.Values[0];
end;

[Run]
Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"; Description: {#LaunchProgram}; Flags: postinstall shellexec
