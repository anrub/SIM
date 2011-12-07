#define MyAppName "S Instant Messenger"
#define MyAppVersion "0.0.2"
#define MyAppPublisher "Flo"
#define MyAppURL "https://github.com/anrub/SIM"

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

[Icons]
Name: "{group}\SIM - S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"; IconFilename: "{app}/icon.ico"
Name: "{userstartup}\SIM - S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"; IconFilename: "{app}/icon.ico"

[Code]
var
  Page: TInputFileWizardPage;
  Database: String;

procedure InitializeWizard;
begin
  
  Page := CreateInputFilePage(wpWelcome,
    'Datenbankpfad', 'Bitte Datenbankdatei auswählen',
    'Bitte die Datenbankdatei auswählen!');

  Page.Add('Speicherort sim.db:',
  'Database File|*.db|All files|*.*',
  '.db');

  Page.Values[0] := 'Z:\Webteam\Mitarbeiter\Fuchs\sim.db';
end;

function GetDatabase(Param: String): String;
begin
  { Return the selected DataDir }
  Result := Page.Values[0];
end;

  