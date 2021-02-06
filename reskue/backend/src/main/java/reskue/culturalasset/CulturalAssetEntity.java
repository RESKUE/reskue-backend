package reskue.culturalasset;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import reskue.ReskueEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;

public class CulturalAssetEntity extends ReskueEntity<CulturalAssetEntity>{
	
	@Column(name = "address", nullable = false)
	private String address = "";
	public static final String ADDRESS = "address";
	public String getAddress() { return this.address; }
	public void setAddress(String address) { this.address = address; }
	
	@Column(name = "longitude", nullable = false)
	private double longitude = 0;
	public static final String LONGITUDE = "longitude";
	public double getLongitude() { return this.longitude; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
	
	@Column(name = "latitude", nullable = false)
	private double latitude = 0;
	public static final String LATITUDE = "latitude";
	public double getLatitude() { return this.latitude; }
	public void setLatitude(double latitude) { this.latitude = latitude; }
	
	@OneToMany(mappedBy = "culturalAsset", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "tasks", nullable = false)
	private List<TaskEntity> tasks = new ArrayList<TaskEntity>();
	public static final String TASKS = "tasks";
	public List<TaskEntity> getTasks() { return this.tasks; }
	public void setTasks(List<TaskEntity> tasks) { this.tasks = tasks; }
	
	@Column(name = "label", nullable = false)
	private String label = "";
	public static final String LABEL = "label";
	public String getLabel() { return this.label; }
	public void setLabel(String label) { this.label = label; }
	
	@Column(name = "level", nullable = false)
	private int level = 0;
	public static final String LEVEL = "level";
	public int getLevel() { return this.level; }
	public void setLevel(int level) { this.level = level; }
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "culturalAssetParent", nullable = false)
	private CulturalAssetEntity culturalAssetParent;
	public static final String CULTURAL_ASSET_PARENT = "culturalAssetParent";
	public CulturalAssetEntity getCulturalAssetParent() { return this.culturalAssetParent; }
	public void setCulturalAssetParent(CulturalAssetEntity culturalAssetParent) { this.culturalAssetParent = culturalAssetParent; }
	
	@OneToMany(mappedBy = "culturalAssetParent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "culturalAssetChildren", nullable = false)
	private List<CulturalAssetEntity> culturalAssetChildren = new ArrayList<CulturalAssetEntity>();
	public static final String CULTURAL_ASSET_CHILDREN = "culturalAssetChildren";
	public List<CulturalAssetEntity> getCulturalAssetChildren() { return this.culturalAssetChildren; }
	public void setCulturalAssetChildren(List<CulturalAssetEntity> culturalAssetChildren) { this.culturalAssetChildren = culturalAssetChildren; }
	
	@OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "notifications", nullable = false)
	private List<NotificationEntity> notifications = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATIONS = "notifications";
	public List<NotificationEntity> getNotifications() { return this.notifications; }
	public void setNotifications(List<NotificationEntity> notifications) { this.notifications = notifications; }
	
	@Override
	public void applyPatch(CulturalAssetEntity details) {
		// TODO Auto-generated method stub
		
	}

}
