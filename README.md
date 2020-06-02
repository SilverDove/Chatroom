# 💻 ChatRoom: Software engineering project 💻
Antoine Mairet - Clara Tricot - Metagang Tabou - Nicolas Jatob - Karen Kaspar - Stella Thammavong

3rd Year at ESIEA

## Introduction


This application is a chatroom to which several users can get connected to in order to discuss securely with each other. It is a Java desktop program for chatting with other users. Discussions are stored persistently and thus they can have access to them when both users are online. It is implemented using a server-client model via which message exchange is done. In order to secure user's messages, AES cryptography algorithm is made use of to encipher them before transmission.

## Prerequisite

* Fetch the release version https://github.com/SilverDove/Chatroom/releases

## Project Execution

To launch the server, please run the ChatRoom class first. You can then run as many client as you want with the Client class.

## Personal involvement

The activities on GitHub absolutely do not represent the personal involvement. Few members of the group found it hard using GitHub's interface, especially with Eclipse because it was causing a lot of problems. For example it would refrain some people to run the programs and would put unidentified errors that can't be found. As we wanted to be as effective as possible and didn't want to impair members of the group's work, the ones that had a hard time working with GitHub worked directly on the code with their teammates. Since Stella worked alone on the GUI by herself and she just modified locally her code to adjust to others remarks. She just gave the GUI code to Nicolas at some point when we needed to merge all the parts. The quantity of push/commit doesn't reflect the personal works because many things have been done outside of GitHub.

Tasks distribution:

- GUI (creation of the whole user interface, corresponding javadoc and UML): Stella
- AES part (creation of a strong and usable encryption/decryption protocol using AES, corresponding javadoc, unit tests and UML) + javadoc of Items: Karen
- Network (creation of a network using a Client/Server approach, corresponding javadoc and UML): Metagang and Nicolas
-  Database (creation of items, database, corresponding javadoc, unit tests and UML): Antoine and Clara


## Overview

* Login Window 

![alt text](https://github.com/SilverDove/Chatroom/blob/master/ImageScreen/logInScreen.png?raw=true)

* New account Window

![alt text](https://github.com/SilverDove/Chatroom/blob/master/ImageScreen/newAccountScreen.png?raw=true)

* Rules Window

![alt text](https://github.com/SilverDove/Chatroom/blob/master/ImageScreen/rulesScreen.png?raw=true)

* Main Window

![alt text](https://github.com/SilverDove/Chatroom/blob/master/ImageScreen/mainScreen.png?raw=true)
