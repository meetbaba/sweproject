import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;


public class Serial implements SerialPortEventListener {

	int count=0;
	private static final String divider=":";
	
    SerialPort serialPort;

        /** ����� �Ƶ��̳�� ����� �ø��� ��Ʈ�� �����ؾ� �Ѵ�. */

    private static final String PORT_NAMES[] = {

            "COM1", // Windows

            };

    /** ��Ʈ���� �����͸� �б� ���� ���۸� ���� input stream */

    private InputStream input;

    /** ��Ʈ�� ���� �Ƶ��̳뿡 �����͸� �����ϱ� ���� output stream */

    private OutputStream output;

    /** ��Ʈ�� ���µǱ� ���� ��ٸ��� ���� �뷫���� �ð�(2��) */

    private static final int TIME_OUT = 2000;

    /** ��Ʈ�� ���� �⺻ ��� �ӵ�, �Ƶ��̳��� Serial.begin�� �ӵ��� ��ġ */

    private static final int DATA_RATE = 9600;

    Resource resource;
    OutputUI outputUI;
    
    public Serial(Resource re, OutputUI outputUI){
    	this.resource=re;
    	this.outputUI=outputUI;
    	
    }
    public void write(String msg) throws IOException{
    	
    	output.write(msg.getBytes());
    }


    public void initialize() {

        CommPortIdentifier portId = null;

        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();



        // ����� ��ǻ�Ϳ��� �����ϴ� �ø��� ��Ʈ�� �� �Ƶ��̳�� �����

                // ��Ʈ�� ���� �ĺ��ڸ� ã�´�.

        while (portEnum.hasMoreElements()) {

            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

            for (String portName : PORT_NAMES) {

                if (currPortId.getName().equals(portName)) {

                    portId = currPortId;

                    break;

                }

            }

        }



        // �ĺ��ڸ� ã�� ������ ��� ����

        if (portId == null) {

            System.out.println("Could not find COM port.");

            return;

        }



        try {

            // �ø��� ��Ʈ ����, Ŭ���� �̸��� ���ø����̼��� ���� ��Ʈ �ĺ� �̸����� ���

            serialPort = (SerialPort) portId.open(this.getClass().getName(),

                    TIME_OUT);



            // �ӵ��� ��Ʈ�� �Ķ���� ����

            serialPort.setSerialPortParams(DATA_RATE,

                    SerialPort.DATABITS_8,

                    SerialPort.STOPBITS_1,

                    SerialPort.PARITY_NONE);



            // ��Ʈ�� ���� �а� ���� ���� ��Ʈ�� ����

            input = serialPort.getInputStream();

            output = serialPort.getOutputStream();



            // �Ƶ��̳�� ���� ���۵� �����͸� �����ϴ� �����ʸ� ���

            serialPort.addEventListener(this);

            serialPort.notifyOnDataAvailable(true);

        } catch (Exception e) {

            System.err.println(e.toString());

        }

    }



    /**

     * �� �޼���� ��Ʈ ����� ������ �� �ݵ�� ȣ���ؾ� �Ѵ�.

     * �������� ���� �÷��������� ��Ʈ ����� �����Ѵ�.

     */

    public synchronized void close() {

        if (serialPort != null) {

            serialPort.removeEventListener();

            serialPort.close();

        }

    }



    /**

     * �ø��� ��ſ� ���� �̺�Ʈ�� ó��. �����͸� �а� ����Ѵ�..

     */

    public synchronized void serialEvent(SerialPortEvent oEvent) {

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {

                int available = input.available();

                byte chunk[] = new byte[available];

                input.read(chunk, 0, available);

                

                //resource setting
                String[] strArray;
                String inputStr=new String(chunk);
                System.out.println("=================: "+inputStr);
                //strArray=inputStr.split(divider);
                //System.out.println("input 0 : "+strArray[0]);
                //System.out.println("input 1 : "+strArray[1]);
                
                //int temperatureInput=(int)Float.parseFloat(strArray[0]);
                //System.out.println("cating temp : "+temperatureInput);
                
                //resource.setTemperature(temperatureInput);
                //resource.defineAirconLevel();
                
                System.out.println("temp : "+resource.getTemperature()+"//aircon level : "+resource.getAirconditioner());
                if(inputStr!=null){
                	
                		if(inputStr.equals("true")){
                        	resource.setSeat(true);
                        	outputUI.setText_seat("Seated");
                        }else if(inputStr.equals("false")){
                        	resource.setSeat(false);
                        	outputUI.setText_seat("Not Seated");
                        }
                        
                	
                }
                
               
            } catch (Exception e) {

                System.err.println(e.toString());

            }

        }

        // �ٸ� �̺�Ʈ ������ �����Ѵ�. ���� ����� �ʿ��� �̺�Ʈ�� ������ �߰��ϸ�ȴ�.

        // �ٸ� �̺�Ʈ�� ���� ���� SerialPortEvent �ҽ��� ����

    }




}