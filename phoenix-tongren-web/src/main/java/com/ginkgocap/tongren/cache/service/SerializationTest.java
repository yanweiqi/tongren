package com.ginkgocap.tongren.cache.service;

public class SerializationTest {
	/**
	 * SerializationTest constructor comment.
	 */
	public SerializationTest() {
		super();
	}

	public static void main(java.lang.String[] args) {

		try {

			System.out.println("Testing a string");

			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream stream = new java.io.ObjectOutputStream(baos);
			stream.writeObject("foo");
			stream.flush();

			String serialized = baos.toString();

			java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(serialized.getBytes());
			java.io.ObjectInputStream inStream = new java.io.ObjectInputStream(bais);

			inStream.readObject();

		} catch (java.io.StreamCorruptedException sce) {

			System.err.println(sce.toString());
			sce.printStackTrace(System.out);

		} catch (java.io.IOException ioe) {

			System.err.println(ioe.toString());
			ioe.printStackTrace(System.out);

		} catch (java.lang.ClassNotFoundException cnfe) {

			System.err.println(cnfe.toString());
			cnfe.printStackTrace(System.out);
		}
		;

		try {

			System.out.println("Testing null");
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream stream = new java.io.ObjectOutputStream(baos);
			stream.writeObject(null);
			stream.flush();

			String serialized = baos.toString();

			java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(serialized.getBytes());
			java.io.ObjectInputStream inStream = new java.io.ObjectInputStream(bais);

			inStream.readObject();

		} catch (java.io.StreamCorruptedException sce) {

			System.err.println(sce.toString());
			sce.printStackTrace(System.out);

		} catch (java.io.IOException ioe) {

			System.err.println(ioe.toString());
			ioe.printStackTrace(System.out);

		} catch (java.lang.ClassNotFoundException cnfe) {

			System.err.println(cnfe.toString());
			cnfe.printStackTrace(System.out);
		}
		;

		try {

			System.out.println("Testing an Integer object");

			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream stream = new java.io.ObjectOutputStream(
					baos);
			stream.writeObject(new Integer(0));
			stream.flush();

			String serialized = baos.toString();

			java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(
					serialized.getBytes());
			java.io.ObjectInputStream inStream = new java.io.ObjectInputStream(
					bais);

			inStream.readObject();

		} catch (java.io.StreamCorruptedException sce) {

			System.err.println(sce.toString());
			sce.printStackTrace(System.out);

		} catch (java.io.IOException ioe) {

			System.err.println(ioe.toString());
			ioe.printStackTrace(System.out);

		} catch (java.lang.ClassNotFoundException cnfe) {

			System.err.println(cnfe.toString());
			cnfe.printStackTrace(System.out);
		}
		;

	}
}
