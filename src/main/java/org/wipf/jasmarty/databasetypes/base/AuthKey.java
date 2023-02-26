package org.wipf.jasmarty.databasetypes.base;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * A Exam.
 */
@Entity
@Table(name = "authKey")
@Cacheable
@RegisterForReflection
public class AuthKey extends PanacheEntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Column(name = "key", nullable = false)
	public String key;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AuthKey)) {
			return false;
		}
		return id != null && id.equals(((AuthKey) o).id);
	}

	@Override
	public String toString() {
		return "id=" + id + " key='" + key;
	}

	public AuthKey update() {
		return update(this);
	}

	public AuthKey persistOrUpdate() {
		return persistOrUpdate(this);
	}

	public static AuthKey update(AuthKey ak) {
		if (ak == null) {
			throw new IllegalArgumentException("can't be null");
		}
		AuthKey entity = AuthKey.<AuthKey>findById(ak.id);
		if (entity != null) {
			entity.key = ak.key;
		}
		return entity;
	}

	public static AuthKey persistOrUpdate(AuthKey ak) {
		if (ak == null) {
			throw new IllegalArgumentException("can't be null");
		}
		if (ak.id == null) {
			persist(ak);
			return ak;
		} else {
			return update(ak);
		}
	}

	public static PanacheQuery<AuthKey> findByKey(String sKey) {
		return find("select authKey from authKey authKey where authKey.name =?1", sKey);
	}

}
