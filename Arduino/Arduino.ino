#include <SoftwareSerial.h>
#include <Servo.h>

Servo myservo;
int pos ;
SoftwareSerial BT(8, 9);
char val;
int phase;
int RED = 3;
int YELLOW = 4;
int GREEN = 5;
int lastSta1;
int lastSta2;
boolean counter;
boolean vied;
boolean phase2Blink;
long currentTime;
long previousTime;
int phase2Interval;
int phase3Interval;
int yellowSta;
int redSta;
int greenSta;
boolean answered;
boolean correct;
int blinkTimes;
int MAX_BLINK_TIMES = 30;
int who;
boolean reseting;
int resetTime;
void(* resetFunc) (void) = 0;

void setup() {
  Serial.begin(9600);
  BT.begin(9600);
  phase = 0;
  pinMode(RED, OUTPUT);
  pinMode(YELLOW, OUTPUT);
  pinMode(GREEN, OUTPUT);
  digitalWrite(RED, HIGH);
  redSta = HIGH;
  digitalWrite(YELLOW, HIGH);
  yellowSta = HIGH;
  digitalWrite(GREEN, HIGH);
  greenSta = HIGH;
  pinMode(6, INPUT);
  pinMode(7, INPUT);
  myservo.attach(10);
  myservo.write(90);
  lastSta1 = 0;
  lastSta2 = 0;
  counter = false;
  vied = false;
  phase2Blink = true;
  currentTime = 0;
  previousTime = 0;
  phase2Interval = 200;
  phase3Interval = 400;
  answered = false;
  blinkTimes = 0;
  who = 0;
  reseting = false;
  resetTime = 0;
  
}

void loop() {
  if(digitalRead(6) == HIGH || digitalRead(7) == HIGH){
    reseting = false;
    resetTime = 0;
  }

  if(digitalRead(6) == LOW && digitalRead(7) == LOW){
    if(!reseting){
      reseting = true;
      resetTime = millis();
    } else{
      if(millis() - resetTime >= 500){
        reseting = false;
        resetTime = 0;
        resetFunc();
      }
    }
  }
  
  switch (phase) {
    case 0:
      if (BT.available()) {
        val = BT.read();
        if (val == 'g')
            phase = 1;
      }
    break;
  case 1:
    if (!counter) {
        delay(1000);
        digitalWrite(RED, LOW);
        redSta = LOW;
        delay(1000);
        digitalWrite(YELLOW, LOW);
        yellowSta = LOW;
        delay(1000);
        digitalWrite(GREEN, LOW);
        greenSta = LOW;
        delay(1000);
        counter = true;
      }
      if (BT.available()) {
        val = BT.read();
        if (val == 'g')
            phase = 2;
      }
    break;
  case 2:
    currentTime = millis();
      if (currentTime - previousTime >= phase2Interval) {
        previousTime = currentTime;
        if (yellowSta == HIGH) {
          yellowSta = LOW;
        } else {
          yellowSta = HIGH;
        }
      }
      digitalWrite(YELLOW, yellowSta);
      if(digitalRead(6) == LOW && lastSta1 == HIGH && ! vied) {
        delay(50);
        vied = true;
        myservo.write(97);
        delay(450);
        myservo.write(90);
        BT.print(1);
        who = 1;
      }
      if(digitalRead(7) == LOW && lastSta2 == HIGH && ! vied) {
        delay(50);
      vied = true;
      myservo.write(80);
        delay(450);
        myservo.write(90);
        BT.print(2);
        who = 2;
      }
      lastSta1 = digitalRead(6);
      lastSta2 = digitalRead(7);
      if (BT.available()) {
      val = BT.read();
        if (val == 'g'){
          phase = 4;
        answered = false;
        }
        else{
          yellowSta = LOW;
      digitalWrite(YELLOW, yellowSta);
        phase = 3;
        }
      }
    break;
  case 3:
      currentTime = millis();
      if (currentTime - previousTime >= phase3Interval) {
        previousTime = currentTime;
        if (yellowSta == HIGH) {
            yellowSta = LOW;
          } else {
            yellowSta = HIGH;
          }
          if (redSta == HIGH) {
            redSta = LOW;
          } else {
            redSta = HIGH;
          }
          if (greenSta == HIGH) {
            greenSta = LOW;
          } else {
            greenSta = HIGH;
          }
      }
      digitalWrite(RED,redSta);
      digitalWrite(YELLOW,yellowSta);
      digitalWrite(GREEN,greenSta);
      if (BT.available()) {
        val = BT.read();
        if (val == 'y'){
          correct = true;
        } else{
          correct = false;
        }
          phase = 4;
        answered = true;
        digitalWrite(RED, LOW);
        redSta = LOW;
        digitalWrite(YELLOW, LOW);
        yellowSta = LOW;
        digitalWrite(GREEN, LOW);
        greenSta = LOW;
      }
      break;
    case 4:
    currentTime = millis();
      if (currentTime - previousTime >= phase2Interval && blinkTimes < MAX_BLINK_TIMES) {
        previousTime = currentTime;
        if(!answered){
          if (yellowSta == HIGH) {
              yellowSta = LOW;
            } else {
              yellowSta = HIGH;
              blinkTimes++;
            } 
        } else if(correct){
          if (greenSta == HIGH) {
              greenSta = LOW;
            } else {
              greenSta = HIGH;
              blinkTimes++;
            }
        } else{
          if (redSta == HIGH) {
              redSta = LOW;
            } else {
              redSta = HIGH;
              blinkTimes++;
            }
        }
      }
      digitalWrite(RED,redSta);
      digitalWrite(YELLOW,yellowSta);
      digitalWrite(GREEN,greenSta);
      if (BT.available()) {
      val = BT.read();
        if (val == '1'){
          phase = 1;
          answered = false;
        digitalWrite(RED, LOW);
        redSta = LOW;
        digitalWrite(YELLOW, LOW);
        yellowSta = LOW;
        digitalWrite(GREEN, LOW);
        greenSta = LOW;
        } else{
          phase = 0;
          counter = false;
        digitalWrite(RED, LOW);
        redSta = LOW;
        digitalWrite(YELLOW, LOW);
        yellowSta = LOW;
        digitalWrite(GREEN, LOW);
        greenSta = LOW;
        }
        lastSta1 = 0;
        lastSta2 = 0;
        counter = false;
        vied = false;
        phase2Blink = true;
        currentTime = 0;
        previousTime = 0;
        phase2Interval = 200;
        phase3Interval = 400;
        answered = false;
        blinkTimes = 0;
        if(who == 1){
          myservo.write(80);
          delay(450);
          myservo.write(90);
          who = 0;
        } else if(who == 2){
          myservo.write(97);
          delay(450);
          myservo.write(90);
          who = 0;
        }
      }
      break;
  }
}
