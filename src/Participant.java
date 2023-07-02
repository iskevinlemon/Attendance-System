
public class Participant {

	private String id;
	private String name;
	private int mobileNum;
	private String AttendanceStatus;
	
	public Participant(String id, String name, int mobileNum, String AttendanceStatus) {
		this.id = id;
		this.name = name;
		this.mobileNum = mobileNum;
		this.AttendanceStatus = AttendanceStatus;
	}

	public String getId() {
		return id;
	}

	public void setNric(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(int mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getAttendanceStatus() {
		return AttendanceStatus;
	}
	public void setAttendanceStatus(String AttendanceStatus) {
		this.AttendanceStatus = AttendanceStatus;
	}
}
