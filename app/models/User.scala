package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(email:String, password:String)

object User {

	// Parser from a ResultSet
	val login = {
		get[String]("email") ~
		get[String]("password") map {
			case email~password => User(email, password)
		}
	}

	// Create a User
	def create(user: User) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					insert into owner values (
						{email},{password}
					)
				"""
			).on(
				'email -> user.email,
				'password -> user.password
			).executeUpdate()
			user
		}
	}

	// Find a user by email
	def findByEmail(email: String): Option[User] = {
	    DB.withConnection { implicit connection =>
	      SQL("select * from owner where email = {email}").on(
	        'email -> email
	      ).as(User.login.singleOpt)
	    }
  	}


	// Retrieve a User
	def authenticate(email: String, password:String): Option[User] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from owner where
					email = {email} and password = {password}
				"""
				).on(
					'email -> email,
					'password -> password
				).as(User.login.singleOpt)
		}
	}

	/* ADITIONAL METHOD */

	// List of Users
	// User.single -> Will create a parser that parse with a single row
	def userList() : List[User] = {
		DB.withConnection { implicit connection =>
			SQL("select * from owner").as(login *)
		}
	}
}