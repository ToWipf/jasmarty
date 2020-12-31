#define VERSION " v0.2"
#define LED_SYSTEM A0
#define LED_BUTTON A1
#define PINS_BUTTONS {2,3,4,5,6,7,8,9}

#include "ST7920_SPI.h"
#include "c64enh_font.h"
#include <SPI.h>
ST7920_SPI lcd(10);

byte incoming;
byte rxbyte;
int in_buttons[] = PINS_BUTTONS;

/*
   setup
*/
void setup() {
  pinMode(LED_SYSTEM, OUTPUT);
  digitalWrite(LED_SYSTEM, HIGH);
  pinMode(LED_BUTTON, OUTPUT);

  Serial.begin(57600);

  for (int nPin : in_buttons) {
    pinMode(nPin, INPUT_PULLUP);
  }

  SPI.begin();
  lcd.init();
  lcd.cls();
  lcd.setFont(c64enh);
  lcd.printStr(ALIGN_CENTER, 28, "Jasmarty F");
  lcd.display(0);

  digitalWrite(LED_SYSTEM, LOW);
}

/*
   get new input via usb
*/
byte serial_getch() {
  while (Serial.available() == 0) {
    taster();
  }
  incoming = Serial.read(); // read the incoming byte:
  return (byte)(incoming & 0xff);
}

/*
   taster abfragen und senden
*/
void taster() {
  for (int nPin : in_buttons) {
    byte nIn = digitalRead(nPin);
    if (nIn == LOW) {
      digitalWrite(LED_BUTTON, HIGH);
      Serial.write(nPin + 47); //pin 2 == 50 == ASCII "2"
      Serial.flush();
      digitalWrite(LED_BUTTON, LOW);
      break;
    }
  }
}

/*
   loop reloadScreen
*/
void loop() {
  digitalWrite(LED_SYSTEM, LOW);

  for (int nLine = 0; nLine < 1024; nLine++) {
    digitalWrite(LED_BUTTON, HIGH);
    lcd.scr[nLine] = serial_getch();
    digitalWrite(LED_BUTTON, LOW);
  }

  lcd.display(0);
  digitalWrite(LED_SYSTEM, HIGH);
}
