#define MyAppName "S Instant Messenger"
#define MyAppVersion "0.0.1"
#define MyAppPublisher "Fuchsi "
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
Source: "sim.jar"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"
Name: "{userstartup}\S Instant Messenger"; Filename: "{sys}\javaw.exe"; Parameters: "-cp sim.jar devhood.im.sim.SimMain -f {code:GetDatabase}"; WorkingDir: "{app}"

[Code]
var
  Page: TInputFileWizardPage;
  Database: String;

procedure InitializeWizard;
begin
  
  Page := CreateInputFilePage(wpWelcome,
    'Datenbankpfad', 'Bitte Datenbankdatei ausw�hlen',
    'Bitte die Datenbankdatei ausw�hlen!');

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

  