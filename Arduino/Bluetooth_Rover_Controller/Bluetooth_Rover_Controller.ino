

/*
 * Arduino Robot Controlled with Android BLE App FTW
 * 
 * Mark Seda and Joy Kitson
 * 
 * 
 * 
 * 
*/

#include <Servo.h>
#include <CurieBLE.h>
 
Servo servoLeft;  // create servo object to control a servo
Servo servoRight;  // create servo object to control a servo
                // twelve servo objects can be created on most boards

#define FORWARD 1525
#define BACKWARD 1475
#define STILL 1500

const int trigPin = 7;
const int echoPin = 6;

int moveDirection = 0; // 0 is none, 1 is FORWARD, 2 back, 3 left, 4 right

unsigned int remainingMoveTime = 0; // number of milliseconds to keep moving in the current state

BLEService moveService("19B10000-E8F2-537E-4F6C-D104768A1214"); // Service for handling moving the robot, and determining it's current relative position 

//TODO: Do we need a separate service for sensor data?

BLEIntCharacteristic moveForward("19B10000-E8F2-537E-4F6C-D104768A1214", BLEWrite);
BLEIntCharacteristic moveBackward("19B10000-E8F2-537E-4F6C-D104768A1214", BLEWrite);
BLEIntCharacteristic turnLeft("19B10000-E8F2-537E-4F6C-D104768A1214", BLEWrite);
BLEIntCharacteristic turnRight("19B10000-E8F2-537E-4F6C-D104768A1214", BLEWrite);

BLEPeripheral myDevice;

//TODO: Add our sensor characteristics

// sensor data is read only
BLEUnsignedLongCharacteristic ultrasonicReading("19B10000-E8F2-537E-4F6C-D104768A1214", BLERead);


void setup()
{

  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  // initialize Serial for debugging
  Serial.begin(9600);

  // set the local name our peripheral (our robot!) advertises
  myDevice.setLocalName("ROBOTO");
  Serial.println("Initializing now");

  // set the UUID for the service the peripheral advertises
  myDevice.setAdvertisedServiceUuid(moveService.uuid());


  // add the service to BLE
  myDevice.addAttribute(moveService);
  // add the various characteristics to our service
  myDevice.addAttribute(moveForward);
  myDevice.addAttribute(moveBackward);
  myDevice.addAttribute(turnLeft);
  myDevice.addAttribute(turnRight);

  // add read only ultrasonic reading.
  myDevice.addAttribute(ultrasonicReading);


  // assign event handlers for connected, disconnected to peripheral
  myDevice.setEventHandler(BLEConnected, blePeripheralConnectHandler);
  myDevice.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);

  // assign event handlers for characteristic
  moveForward.setEventHandler(BLEWritten, moveForwardCharacteristicWritten);
  moveBackward.setEventHandler(BLEWritten, moveBackwardCharacteristicWritten);
  turnLeft.setEventHandler(BLEWritten, turnLeftCharacteristicWritten);
  turnRight.setEventHandler(BLEWritten, turnRightCharacteristicWritten);
  
  
  servoLeft.attach(10);  // attaches the servo on pin 9 to the servo object
  servoRight.attach(11);

  // Initialize the servos to not be moving
  servoLeft.writeMicroseconds(STILL);
  servoRight.writeMicroseconds(STILL);

  Serial.println("Done Initializing");

  // start advertising
  myDevice.begin();

  Serial.println(("Bluetooth device active, waiting for connections..."));
}

void loop()
{
  //myDevice.poll();

  // update the ultrasonicReading repeatedly.
  ultrasonicReading.setValue(getDistance());

  if(moveDirection > 0)
  {
    switch(moveDirection){
      case(0):
        servoLeft.writeMicroseconds(STILL);
        servoRight.writeMicroseconds(STILL);
        break;
      case(1):
        servoLeft.writeMicroseconds(BACKWARD);
        servoRight.writeMicroseconds(FORWARD);
        break;
      case(2):
        servoLeft.writeMicroseconds(FORWARD);
        servoRight.writeMicroseconds(BACKWARD);
        break;
      case(3):
        servoLeft.writeMicroseconds(FORWARD);
        servoRight.writeMicroseconds(FORWARD);
        break;
      case(4):
        servoLeft.writeMicroseconds(BACKWARD);
        servoRight.writeMicroseconds(BACKWARD);
        break;
    }
    moveDirection = -1;
  }
  else if(moveDirection == -1)
  { // stop moving!
    if(remainingMoveTime < millis())
    {
      Serial.println("Stopping Moving!");
      servoLeft.writeMicroseconds(STILL);
      servoRight.writeMicroseconds(STILL);
      moveDirection = 0;
    } 
  }

  delay(500);
  //delay(5000);
}


void blePeripheralConnectHandler(BLECentral& central) {
  // central connected event handler
  Serial.print("Connected event, central: ");
  Serial.println(central.address());
}

void blePeripheralDisconnectHandler(BLECentral& central) {
  // central disconnected event handler
  Serial.print("Disconnected event, central: ");
  Serial.println(central.address());
}


void moveForwardCharacteristicWritten(BLECentral& central, BLECharacteristic& characteristic) {
  // central wrote new value to characteristic, update LED
  Serial.println("Characteristic event");

  if (moveForward.value()) {
    Serial.println("Moving Forward " + String(moveForward.value()) + " milliseconds.");

    moveDirection = 1;
    remainingMoveTime = millis() + moveForward.value();
  }
}


void moveBackwardCharacteristicWritten(BLECentral& central, BLECharacteristic& characteristic) {
  // central wrote new value to characteristic, update LED
  Serial.println("Characteristic event");

  if (moveBackward.value()) {
    Serial.println("Moving Backward for " + String(moveBackward.value()) + " milliseconds.");

    moveDirection = 2;
    remainingMoveTime = millis() + moveBackward.value();
  }
}

void turnLeftCharacteristicWritten(BLECentral& central, BLECharacteristic& characteristic) {
  // central wrote new value to characteristic, update LED
  Serial.println("Characteristic event");

  if (turnLeft.value()) {
    Serial.println("Turning left for " + String(turnLeft.value()) + " milliseconds.");

    moveDirection = 3;
    
    remainingMoveTime = millis() + turnLeft.value();
  }
}

void turnRightCharacteristicWritten(BLECentral& central, BLECharacteristic& characteristic) {
  // central wrote new value to characteristic, update LED
  Serial.println("Characteristic event");

  if (turnRight.value()) {
    Serial.println("Turning right for " + String(turnRight.value()) + " milliseconds.");

    moveDirection = 4;

    remainingMoveTime = millis() + turnRight.value();
  } 
}

unsigned long getDistance()
{
  unsigned long duration;
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(5);
  digitalWrite(trigPin, LOW);

  duration = pulseIn(echoPin, HIGH); //73 microseconds per inchd

  Serial.println("Distance: " + String(duration));

  return duration;
}
