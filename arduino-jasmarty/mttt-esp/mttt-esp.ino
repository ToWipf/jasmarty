#include <ESP8266WiFi.h>
#include <WiFiClient.h>
//#include <ESP8266mDNS.h>
#include <ESP8266HTTPClient.h>
#include <FastLED.h>

#include <Wire.h>
#include <Adafruit_SSD1306.h>

#define STASSID           "UUID"
#define STAPSK            "KEY"
#define APISERVER1        "http://192.168.2.55:8080/"
#define APISERVER2        "http://liveserver:34907/"
#define SERVERHEADER_KEY  "x-myKey"
#define SERVERHEADER_VAL  "myVal"
#define LED_ONBOARD       D4
#define LED_EINS          D8
#define BUTTON_CLS_PIN    D3
#define BUTTON_DO_PIN     D7
#define BUTTON_START_PIN  D6
#define LED_PIN           D5
#define POTI_PIN          A0
#define NUM_LEDS          225 // 15x15 = 225

#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels
#define OLED_RESET     -1 // Reset pin # (or -1 if sharing Arduino reset pin)

CRGB leds[NUM_LEDS];
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

const char *ssid = STASSID;
const char *password = STAPSK;
String apiserver = "-";
boolean hasClicked = false;
int idPoti = 0;

/*

*/
void doGetDataFromServerDO(String path) {
  digitalWrite(LED_EINS, HIGH);
  display.setCursor(0, 50);
  display.println("    ");
  display.display();
  WiFiClient wifiClient;
  HTTPClient httpClient;

  httpClient.begin(wifiClient, apiserver + path);
  httpClient.addHeader(SERVERHEADER_KEY, SERVERHEADER_VAL);
  int httpCode = httpClient.GET();

  if (httpCode == HTTP_CODE_OK) {
    int len = httpClient.getSize();
    // create buffer for read
    uint8_t buff[6] = { 0 };

    // get tcp stream
    WiFiClient * stream = &wifiClient;

    // read all data from server
    int LED_NR = 0;
    int LED_R = 0;
    int LED_G = 0;
    int LED_B = 0;
    while (httpClient.connected() && (len > 0 || len == -1)) {
      // read up to 128 byte
      int c = stream->readBytes(buff, std::min((size_t)len, sizeof(buff)));

      LED_NR = (buff[0] - 48) * 100 + (buff[1] - 48) * 10  + (buff[2] - 48);

      //LED_NR = buff[0];
      LED_R = buff[3];
      LED_G = buff[4];
      LED_B = buff[5];

      if (LED_NR < NUM_LEDS) {
        leds[LED_NR] = CRGB ( LED_R, LED_G, LED_B);
        FastLED.show();
      }

      if (len > 0) {
        len -= c;
      }
    }
    //FastLED.show();
    digitalWrite(LED_EINS, LOW);
  }
  display.setCursor(0, 50);
  display.println(httpCode);
  display.display();

  httpClient.end();
}

void blinkLED(int id) {
  CRGB old = leds[id];
  leds[id] = CRGB ( 55, 55, 55);
  display.setCursor(30, 20);
  display.println((String)id + " ");
  display.display();
  FastLED.show();
  leds[id] = old;
  FastLED.show();
}

/*

*/
void setup() {
  //Serial.begin(115200);
  //Serial.println("Wipf42");
  pinMode(LED_ONBOARD, OUTPUT);
  pinMode(LED_EINS, OUTPUT);
  pinMode(BUTTON_DO_PIN, INPUT_PULLUP);
  pinMode(BUTTON_CLS_PIN, INPUT_PULLUP);
  pinMode(BUTTON_START_PIN, INPUT_PULLUP);

  digitalWrite(LED_EINS, 1);

  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    //Serial.println("SSD1306 failed");
    //digitalWrite(LED_EINS, 0);
  }
  display.display();
  display.clearDisplay();
  display.setCursor(0, 0);
  display.setTextSize(1);
  display.setTextColor(WHITE, BLACK);
  display.println("WLAN");
  //display.drawPixel(10, 10, WHITE);
  display.display();

  FastLED.addLeds<WS2812, LED_PIN, GRB>(leds, NUM_LEDS);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    display.print(".");
    display.display();
  }

  if (!digitalRead(BUTTON_CLS_PIN)) {
    apiserver = APISERVER1;
  } else {
    apiserver = APISERVER2;
  }

  display.clearDisplay();
  display.setCursor(0, 0);
  display.println(apiserver);
  doGetDataFromServerDO("mttt/full");
  display.setCursor(0, 20);
  display.println("Led:");
  display.display();

  digitalWrite(LED_EINS, 0);
}

/*

*/
void loop(void) {

  //if (millis() % 500 == 1) {
  idPoti  = analogRead(POTI_PIN) / 4.5;
  if (idPoti <= NUM_LEDS) {
    blinkLED(idPoti);
  }
  //}

  if (!digitalRead(BUTTON_DO_PIN) && idPoti <= NUM_LEDS) {
    display.setCursor(0, 40);
    display.println(F("Click DO"));
    display.display();
    //doGetDataFromServerDO("mttt/do/" + (String)in_X + "/" + (String)in_Y );
    doGetDataFromServerDO("mttt/doId/" + (String)idPoti );
    hasClicked = true;
  }

  if (!digitalRead(BUTTON_CLS_PIN)) {
    display.setCursor(0, 40);
    display.println(F("Click CLS"));
    display.display();
    doGetDataFromServerDO("mttt/cls");
    hasClicked = true;
  }

  if (!digitalRead(BUTTON_START_PIN)) {
    display.setCursor(0, 40);
    display.println(F("Click Start"));
    display.display();
    doGetDataFromServerDO("mttt/start");
    hasClicked = true;
  }

  if (hasClicked) {
    delay(100);
    display.setCursor(0, 40);
    display.println(F("           "));
    display.display();
    hasClicked = false;
  }
}