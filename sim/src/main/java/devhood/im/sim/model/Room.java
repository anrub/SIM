package devhood.im.sim.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Room extends AbstractPersistable<Long> {
	@Column(unique = true)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "rooms")
	@Column(nullable = true)
	private List<User> users = new ArrayList<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
