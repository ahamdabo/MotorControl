/*
 ** @ Authors : 1-Ahmed Assaf
 **             2-Ahmed Abo El Hassan
 **             3-Mohammad Alaa Hekal
 **             4-Afaf Hassan
 **
 **     STEPPER MOTOR CONTROL USING PIC16F628A
 **                   ITI
 */

//Pin Definition
sbit m1 at PORTB.b4;
sbit m2 at PORTB.b5;
sbit m3 at PORTB.b6;//motor control pins
sbit m4 at PORTB.b7;

unsigned short motor_direction at TRISB;// motor control pins direction

bit flag;  // direction flag
bit stop;  // stop flag
unsigned speed;     // delay value
unsigned char ch;  // recieved uart byte

void interrupt() {
	INTCON.GIE = 0;       // disable global interrupt
	if (PIR1.RCIF)       // Rx interrupt flag
	{
		PIR1.RCIF = 0;       // clear usart interrupt flag
		ch = RCREG;          // read the Rx register
		if (ch >= 49 && ch <= 57)
			stop = 0;    //  if ch not equal 0
		switch (ch) {
		case 'c':
			flag = 1;
			break;   // anticlockwise
		case 'd':
			flag = 0;
			break;   // clockwise

		case '0':
			stop = 1;
			break;
		case '1':
			stop = 0;
			speed = 150;
			break;
		case '2':
			stop = 0;
			speed = 100;
			break;
		case '3':
			stop = 0;
			speed = 80;
			break;
		case '4':
			stop = 0;
			speed = 60;
			break;   // speed control
		case '5':
			stop = 0;
			speed = 50;
			break;
		case '6':
			stop = 0;
			speed = 40;
			break;
		case '7':
			stop = 0;
			speed = 30;
			break;
		case '8':
			stop = 0;
			speed = 20;
			break;
		case '9':
			stop = 0;
			speed = 5;
			break;

		}
	}

	INTCON.GIE = 1;
}

void main() {
// pin initialization
	//flag=0;
	stop = 1;
	m1 = m2 = m3 = m4 = 0;  // initialize motor control pins
	motor_direction = 0x00;

	trisb.b1 = 1;    // Rx
	trisb.b2 = 0;    // Tx

	TXSTA.SYNC = 0; // set usart as asyncronus
	TXSTA.BRGH = 1; // high baud rate
	TXSTA.TX9 = 0;  // select 8-bit transimison

	RCSTA.SPEN = 1; // enable serial port
	RCSTA.RX9 = 0;  // select 8-bit transimison
	RCSTA.CREN = 1; // enable continous recieve
	RCSTA.ADEN = 0; // disable address detect

	SPBRG = 25;     // baud rate 9600

	INTCON.GIE = 1; // enable global interrupt
	INTCON.PEIE = 1; // enable peripheral interrupt
	PIR1.RCIF = 0; // clear recieve interrupt flag
	PIE1.RCIE = 1; // enable usart recieve interrupt

	while (1) {
		if (!stop) {
			while (flag && !stop) {
				// AntiClockWise
				m2 = m3 = 0;
				m1 = m4 = 1;  // 0b 1001 0000
				vDelay_ms(speed);
				m1 = m3 = 0;
				m2 = m4 = 1;  // 0b 1010 0000
				vDelay_ms(speed);
				m2 = m3 = 1;
				m1 = m4 = 0;  //0b 0110 0000
				vDelay_ms(speed);
				m2 = m4 = 0;
				m1 = m3 = 1; // 0b 0101 0000
				vDelay_ms(speed);
			}
			while (!flag && !stop) {
				// clockwise
				m2 = m4 = 0;
				m1 = m3 = 1; // 0b 0101 0000
				vDelay_ms(speed);
				m2 = m3 = 1;
				m1 = m4 = 0;   //0b 0110 0000
				vDelay_ms(speed);
				m1 = m3 = 0;
				m2 = m4 = 1;  // 0b 1010 0000
				vDelay_ms(speed);
				m2 = m3 = 0;
				m1 = m4 = 1;   // 0b 1001 0000
				vDelay_ms(speed);
			}
		}
	}

}
