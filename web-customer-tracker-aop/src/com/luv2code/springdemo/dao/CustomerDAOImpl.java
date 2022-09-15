package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.util.SortUtils;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	// @Transactional we are have move this annotation to service layer
	public List<Customer> getCustomers(int theSortField) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		String theFieldNamd = null;

		switch (theSortField) {
		case SortUtils.FIRST_NAME:
			theFieldNamd = "first_name";
			break;
		case SortUtils.LAST_NAME:
			theFieldNamd = "last_name";
			break;
		case SortUtils.EMAIL:
			theFieldNamd = "email";
			break;
		default:
			theFieldNamd = "last_name";
		}

		
		//create a query 
		String query="from Customer order by "+theFieldNamd;
		
		Query<Customer> theQuery=currentSession.createQuery(query,Customer.class);

		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();

		// return the results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// save/update the customer ... finally LOL

		currentSession.saveOrUpdate(theCustomer);

	}

	@Override
	public Customer getCustomer(int theId) {

		// get the current hibernate session
		Session cureentSession = sessionFactory.getCurrentSession();

		// now retrieve /read from database using the primary key
		Customer theCustomer = cureentSession.get(Customer.class, theId);

		return theCustomer;
	}

	@Override

	public void deleteCustomer(int theId) {

		Session cureentSession = sessionFactory.getCurrentSession();

		Query theQuery = cureentSession.createQuery("delete from Customer where id=:theCustomerId");
		theQuery.setParameter("theCustomerId", theId);
		theQuery.executeUpdate();

	}

	@Override
	public List<Customer> searchCustomer(String theSearchName) {

		Session currentSession = sessionFactory.getCurrentSession();

		Query theQuery = null;

		if (theSearchName != null && theSearchName.trim().length() > 0) {
			// search for firstName or lastName ... case insensitive
			theQuery = currentSession.createQuery(
					"from Customer where lower(firstName) like :theName or lower(lastName) like :theName",
					Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		} else {
			// theSearchName is empty ... so just get all customers
			theQuery = currentSession.createQuery("from Customer", Customer.class);
		}

		List<Customer> theCustomers = theQuery.getResultList();

		theCustomers.stream().forEach(t -> System.out.println());

		return theCustomers;
	}

}
