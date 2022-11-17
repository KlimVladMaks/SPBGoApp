import os

import telebot

API_TOKEN = os.getenv('API')
chat_id = os.getenv('CHAT')
bot = telebot.TeleBot(API_TOKEN, parse_mode=None)

doc = open('app-debug.apk', 'rb')
bot.send_document(chat_id, doc)
bot.send_document(chat_id, "FILEID")
