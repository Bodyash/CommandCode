package me.bodyash.redeemcode.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DAOHib implements DAO{
	
	private SessionFactory sf;
	
	public DAOHib(SessionFactory sf) {
		this.sf = sf;
	}
	
	

	@Override
	public void addCode(String type, String code) {
		try (Session sess = getSession()){
			sess.beginTransaction();
			Code newcode = new Code();
			newcode.setCode(code);
			newcode.setCodetype(type);
			sess.save(newcode);
			sess.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean removeCode(String code) {
		try (Session sess = getSession()){
			sess.beginTransaction();
			Query q = sess.createQuery("SELECT c FROM Code WHERE c.code=" + code);
			List res = q.getResultList();
			if (res.isEmpty()){
				return false;
			}else{
				sess.delete(res.get(0));
				sess.getTransaction().commit();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkCode(String code) {
		try (Session sess = getSession()){
			sess.beginTransaction();
			Query q = sess.createQuery("SELECT c FROM Code WHERE c.code=" + code);
			List res = q.getResultList();
			if (!res.isEmpty()){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void removeAllCodes() {
		try (Session sess = getSession()){
			sess.beginTransaction();
			Query q = sess.createQuery("DROP TABLE CODE");
			q.executeUpdate();
			sess.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public String getCodeType(String code) {
		try (Session sess = getSession()){
			sess.beginTransaction();
			Query q = sess.createQuery("SELECT c FROM Code WHERE c.code=" + code);
			List res = q.getResultList();
			Code newcode = (Code) res.get(0);
			return newcode.getCodetype();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//i`m cheking for NULL, before using the codetype C: Believe me!
		return null;
	}
	
	private Session getSession() throws HibernateException {
		return sf.openSession();
	}

}
