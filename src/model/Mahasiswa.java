package model;

public class Mahasiswa {
    private int id;
    private int no;
    private int nim;
    private String kelas;
    private double ipk;
    private int semester;
    private int totalSks;

    public Mahasiswa(int id, int no, int nim, String kelas, double ipk, int semester, int totalSks) {
        this.id = id;
        this.no = no;
        this.nim = nim;
        this.kelas = kelas;
        this.ipk = ipk;
        this.semester = semester;
        this.totalSks = totalSks;
    }

    public int getId() { return id; }
    public int getNo() { return no; }
    public int getNim() { return nim; }
    public String getKelas() { return kelas; }
    public double getIpk() { return ipk; }
    public int getSemester() { return semester; }
    public int getTotalSks() { return totalSks; }
} 