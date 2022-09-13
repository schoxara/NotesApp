package com.tkusevic.stadiums.commons.constants

//registration request
const val SUCCESS_REGISTRATION = "Successful registration!"
const val FAILED_REGISRATION = "Failed registration!"

const val BASE_URL = "https://notes-mqtt.herokuapp.com/"

const val RESPONSE_OK = 200
const val RESPONSE_OK_201 = 201
const val RESPONSE_NOT_OK = 422
const val RESPONSE_CONFLICT = 409

// mqtt
const val MQTT_HOST = "broker.emqx.io"
const val MQTT_PORT = "1883"


//registration errors
const val PASSWORD_ERROR = "Password must contains 6 or more characters!"
const val EMAIL_ERROR = "Incorrect email!"
const val NO_NAME_ERROR = "Name is empty!"
const val ERROR_EMAIL_OR_PASSWORD = "Wrong input of email or password"

//pager titles
const val ALL_NOTES = "All Notes"
const val MY_NOTES = "My Notes"


//keys for activities
const val NOTES_KEY = "notes"

//shared preferences
const val PREFS_USER_ID_KEY = "userId"
const val PREFS_NAME = "AppPrefs"
const val PREFS_USER_TOKEN = "userToken"

//email regex
const val EMAIL_REGEX =
    "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"


