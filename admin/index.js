const fs = require('fs');
const readline = require('readline');
const {google} = require('googleapis');
const colors = require('colors');
// If modifying these scopes, delete token.json.
const SCOPES = ['https://www.googleapis.com/auth/drive.metadata.readonly',
                'https://www.googleapis.com/auth/drive',
                'https://www.googleapis.com/auth/drive.file',
                'https://www.googleapis.com/auth/drive.appdata'];
// The file token.json stores the user's access and refresh tokens, and is
// created automatically when the authorization flow completes for the first
// time.
const TOKEN_PATH = 'token.json';
let authToken = null;

// Load client secrets from a local file.
fs.readFile('credentials.json', (err, content) => {
  if (err) return console.log('Error loading client secret file:', err);
  // Authorize a client with credentials, then call the Google Drive API.
//   authorize(JSON.parse(content), listFiles);
helpMessage();
  authorize(JSON.parse(content), runCommand);

});


const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

function prompt() {
  var arrow = '=> ', length = arrow.length;
  rl.setPrompt(arrow.white, length);
  rl.prompt();
}


/**
 * Create an OAuth2 client with the given credentials, and then execute the
 * given callback function.
 * @param {Object} credentials The authorization client credentials.
 * @param {function} callback The callback to call with the authorized client.
 */
function authorize(credentials, callback) {
  const {client_secret, client_id, redirect_uris} = credentials.installed;
  const oAuth2Client = new google.auth.OAuth2(
      client_id, client_secret, redirect_uris[0]);

  // Check if we have previously stored a token.
  fs.readFile(TOKEN_PATH, (err, token) => {
    if (err) return getAccessToken(oAuth2Client, callback);
    oAuth2Client.setCredentials(JSON.parse(token));
    authToken = oAuth2Client;
    callback(oAuth2Client);
  });
}

/**
 * Get and store new token after prompting for user authorization, and then
 * execute the given callback with the authorized OAuth2 client.
 * @param {google.auth.OAuth2} oAuth2Client The OAuth2 client to get token for.
 * @param {getEventsCallback} callback The callback for the authorized client.
 */
function getAccessToken(oAuth2Client, callback) {
  const authUrl = oAuth2Client.generateAuthUrl({
    access_type: 'offline',
    scope: SCOPES,
  });
  console.log('Authorize this app by visiting this url:', authUrl);
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });
  rl.question('Enter the code from that page here: ', (code) => {
    rl.close();
    oAuth2Client.getToken(code, (err, token) => {
      if (err) return console.error('Error retrieving access token', err);
      oAuth2Client.setCredentials(token);
      // Store the token to disk for later program executions
      fs.writeFile(TOKEN_PATH, JSON.stringify(token), (err) => {
        if (err) return console.error(err);
        console.log('Token stored to', TOKEN_PATH);
      });
      callback(oAuth2Client);
    });
  });
}

/**
 * Lists the names and IDs of up to 10 files.
 * @param {google.auth.OAuth2} auth An authorized OAuth2 client.
 */
function listFiles(auth) {
  const drive = google.drive({version: 'v3', auth});
  return new Promise( (resolve, reject) => {
    drive.files.list({
        pageSize: 10,
        fields: 'nextPageToken, files(id, name)',
      }, (err, res) => {
        if (err) return console.log('The API returned an error: '.red + err);
        const files = res.data.files;
        // console.log(files);
        if (files.length) {
          console.log('Files:'.brightMagenta.bold);
          files.map((file) => {
            console.log(`[FILE NAME] ${file.name}`.brightCyan.bold +`(${file.id})`.brightCyan);
          });
        } else {
          console.log('No files found.'.red);
        }
      });
  })

}

/**
 * Lists the names and IDs of up to 10 files.
 * @param {google.auth.OAuth2} auth An authorized OAuth2 client.
 */
function listFilesSize(auth) {
    const drive = google.drive({version: 'v3', auth});
    return new Promise( (resolve, reject) => {
      drive.files.list({
          pageSize: 10,
        //   fields: 'nextPageToken, files(id, name, size, modifiedTime)',
        fields: '*',
        corpora: 'user'
        }, (err, res) => {
          if (err) return console.log('The API returned an error: '.red + err);
          const files = res.data.files;
        //   console.log(files);
          if (files.length) {
              console.log("Files: [With Sizes]".brightMagenta.bold)
            files.map((file) => {
                console.log(`[FILE NAME]: ${file.name}`.brightCyan.bold + ` (${file.id}) `.brightCyan);
                console.log(`\t File-size: (${file.size} bytes)`.brightBlue);
            });
            prompt();
          } else {
            console.log('No files found.'.red);
          }
        });
    })
}

/**
 * Lists the names and IDs of up to 10 files.
 * @param {google.auth.OAuth2} auth An authorized OAuth2 client.
 */
function listFilesModification(auth) {
    const drive = google.drive({version: 'v3', auth});
    return new Promise( (resolve, reject) => {
      drive.files.list({
        pageSize: 10,
        //fields: 'nextPageToken, files(id, name, size, modifiedTime)',
        fields: 'nextPageToken, files(name, id, modifiedTime, lastModifyingUser)',
        }, (err, res) => {
          if (err) return console.log('The API returned an error: ' + err);
          const files = res.data.files;
        //   console.log(files);
          if (files.length) {
              console.log("Files: [With Last Modified User and Last Modification Date".brightMagenta.bold)
            files.map((file) => {
                console.log(`[FILE NAME]: "${file.name}" `.brightCyan.bold +`(${file.id}) `.brightCyan);
                console.log(`\tLast Modified By - [NAME]: ${file.lastModifyingUser.displayName} [EMAIL]: ${file.lastModifyingUser.emailAddress}`.brightBlue);
                console.log(`\tLast Modified At: ${file.modifiedTime}`.brightBlue);
            });
            prompt();
          } else {
            console.log('No files found.');
          }
        });
    })
}

function deleteFile(OAuth, fileId_toDelete) {
    const drive = google.drive({version: 'v3', auth: OAuth});
    drive.files.delete({
        fileId: fileId_toDelete
    }, (err, res) => {
        if (err) return console.log('The API returned an error: ' + err);
        console.log(`Sucessfully deleted file: ${fileId_toDelete}`.brightGreen);
    })
   
  }


  
function runCommand(auth){


      prompt();

    rl.on('SIGINT', () => {
        console.log('Exiting Clowns who Code Admin Interface'.brightGreen);
        rl.close();
      });

    rl.on('SIGTSTP', () => {
        console.log('Exiting Clowns who Code Admin Interface'.brightGreen);
        rl.close();
      });

    rl.on('line', (input) => {
        input = input.trim();
        args = input.split(" ");
        input = args[0];
        if(input === "" || input === "-h" || input === "--help" || input === "help"){
            helpMessage();
        }
        else if(input === "-l" || input === "--list" || input === "l"){
            listFiles(auth)
        }
        else if(input === "exit" || input === "quit" || input === "-q"){
            console.log("Thank you for using the clowns who code admin interface... exiting".brightGreen)
            rl.close();
        }
        else if(input === "-ls" || input === "--listsize" || input === "ls"){
            listFilesSize(auth);            
        }
        else if(input === "-d" || input === "--delete" || input === "delete"){
            if(args.length === 2){
                deleteFile(authToken, args[1])
            } else if (args.length > 2){
              for(let i = 1; i < args.length; i++){
                deleteFile(authToken, args[i]);
              }
              
            }else {
                rl.question("Give file id to delete:".brightMagenta, (answer) => {
                    answer = answer.trim();
                    deleteFile(authToken, answer);
                });
            }
            prompt();
            
        }
        else if(input === '-lm' || input === '--listmodification' || input === "lm"){
            listFilesModification(auth)
        } else {
            console.log("Invalid command please type -h | --help to see valid commands".brightRed);
            prompt();
        }
        //prompt();
    });

}


function helpMessage(){
    let ques = `\n[[ Welcome to the Clowns who Code Admin Interface ]]\n`.rainbow.bold.underline.italic;
            ques += `  Please run one of the following commands:\n`.brightMagenta.bold;
            ques += `  -h | --help:  Display help message\n`.brightCyan;
            ques += `  -d | --delete:  Delete a file\n`.brightCyan;
            ques += `  -l | --list:  Lists all current files hosted on the drive\n`.brightCyan;
            ques += `  -ls | --listsize:  Lists the files with file size included\n`.brightCyan;
            ques += `  -lm | --listmodification:  Lists user which last modified each file along with the time\n`.brightCyan;
            ques += `  exit | quit:  Exit the Clowns who Code Command Line Interface`.brightCyan;
    console.log(ques);
    prompt();

}