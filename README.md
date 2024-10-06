# EnigmaIRC v2
EnigmaIRC v2 *(EnigmaIRC-Java)* - an updated version of the previously known EnigmaIRC-v1 with a large number of changes and innovations.

## What is it?
EnigmaIRC is a secure open-source messenger that uses symmetric end-to-end encryption. It is intended to work only from a computer. EnigmaIRC does not have a single center - each user <u>creates his own server</u>.

## How to install?
To install EnigmaIRC, go to the [latest release page](https://github.com/Dertfin3051/EnigmaIRC-Java/releases/latest) and download the jar files for the client and server. Since the messenger is written entirely in Java, you can easily run it on different operating systems, be it Windows, Linux or MacOS.

### Server setup
To use the messenger, you need to have your own or rented server.

**Step 1**
Connect to the server console. The most popular way to do this is SSH. Usually, the host provider indicates the connection instructions on their website.

**Step 2**
Install [Java](https://www.oracle.com/cis/java/technologies/downloads/#java21) version 21 or higher. There are many articles on the Internet on how to do this on different OS.

**Step 3**
Install EnigmaIRC Server. If you only have access to the server terminal, then this can be done via SFTP or WGET. I will show the latter method.

On your main PC, hover over the file with the latest version of the server, right-click on it and click "Copy link".
Next, go back to the server terminal and enter:
`wget <your_link>`

A jar file will be installed on your server. Next, find out its name using the `ls` command and enter the command below to start the server:
`java -jar <downloaded_file_name>`

Before the program starts, you will see a number that you need to copy and enter
`disown <number_when_starting_the_program>`

The server is ready for use! If desired, you can configure it by editing the generated `config.json` file

### Installing the client
Everything is much simpler with the client. On the latest release page, just click on the file with the client and it will be installed on your PC.

Next, you still need to install Java 21 or higher.

To run the client, open the console and enter
`java -jar <downloaded_file_name>`

The `config.json` file will appear in the folder where you downloaded the program, which is <u>required</u> for configuration.

### Client configuration
Open the `config.json` file in any program convenient for you. After the first launch of EnigmaIRC, you will have a `newkey.txt` file containing a new encryption key. It must be written in the `security-key` field:
```json
...
"security-key" : "key from file"
...
```
Be sure to check that there are no extra spaces before and after the key! The key must be saved and given to your friend/interlocutor in offline mode.

Next, set your preferred username in the `username` field.
In the `server-address` field, specify the public IP address of the server you rented, and in the `server-port` field, its port, which is specified in the settings. If you have not changed anything, then the server port should be `6667`.

Done! All that remains is to help your interlocutor with setting up the messenger and you can start chatting securely. It is highly recommended not to transfer the settings, and especially the security key, over the Internet, but it is better to do this on a flash drive or a piece of paper *(yes, this is also possible)* 
