/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.core.fakedata;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.clock.ClockService;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class FakeDataService  {

    private Faker javaFaker;

    private Random random;
    private RandomService randomService;
    private FakeValuesService fakeValuesService;

    @Programmatic
    @PostConstruct
    public void init() {

        random = RandomUtils.JVM_RANDOM;
        javaFaker = new Faker(random);

        randomService = new RandomService(random);
        fakeValuesService = new FakeValuesService(Locale.ENGLISH, randomService);

        // wrappers for the javafaker subclasses
        this.name = new Name(fakeValuesService);
        this.comms = new Comms(this.name.javaFakerName, fakeValuesService);
        this.lorem = new Lorem(fakeValuesService, randomService);
        this.address = new Address(this.name.javaFakerName, fakeValuesService, randomService);
        this.creditCard = new CreditCard(fakeValuesService);
        this.book = new Book(randomService);

        this.strings = new Strings();
        this.bytes = new Bytes();
        this.shorts = new Shorts();
        this.integers = new Integers();
        this.longs = new Longs();
        this.floats = new Floats();
        this.doubles = new Doubles();
        this.chars = new Chars();
        this.booleans = new Booleans();

        this.collections = new Collections();
        this.localDates = new LocalDates();
        this.periods = new Periods();
    }

    private Name name;
    private Comms comms;
    private Lorem lorem;
    private Address address;
    private CreditCard creditCard;
    private Book book;

    private Strings strings;
    private Bytes bytes;
    private Shorts shorts;
    private Integers integers;
    private Longs longs;
    private Floats floats;
    private Doubles doubles;
    private Chars chars;
    private Booleans booleans;
    private Collections collections;
    private LocalDates localDates;
    private Periods periods;


    /**
     * Access to the full API of the underlying javafaker library.
     */
    @Programmatic
    public Faker javaFaker() { return javaFaker; }

    // //////////////////////////////////////

    @Programmatic
    public Name name() {
        return name;
    }

    @Programmatic
    public Comms comms() {
        return comms;
    }

    @Programmatic
    public Lorem lorem() {
        return lorem;
    }

    @Programmatic
    public Address address() {
        return address;
    }

    @Programmatic
    public CreditCard creditCard() {
        return creditCard;
    }

    @Programmatic
    public Book book() {
        return book;
    }

    // //////////////////////////////////////

    @Programmatic
    public Bytes bytes() {
        return bytes;
    }

    @Programmatic
    public Shorts shorts() {
        return shorts;
    }

    @Programmatic
    public Integers ints() {
        return integers;
    }

    @Programmatic
    public Longs longs() {
        return longs;
    }

    @Programmatic
    public Floats floats() {
        return floats;
    }

    @Programmatic
    public Doubles doubles() {
        return doubles;
    }

    @Programmatic
    public Chars chars() {
        return chars;
    }

    @Programmatic
    public Booleans booleans() {
        return booleans;
    }

    @Programmatic
    public Strings strings() {
        return strings;
    }

    // //////////////////////////////////////

    @Programmatic
    public Collections collections() {
        return collections;
    }

    @Programmatic
    public LocalDates localDates() {
        return localDates;
    }

    @Programmatic
    public Periods periods() {
        return periods;
    }

    // //////////////////////////////////////

    public class Name {
        com.github.javafaker.Name javaFakerName;
        Name(final FakeValuesService fakeValuesService) {
            javaFakerName = new com.github.javafaker.Name(fakeValuesService);
        }

        @Programmatic
        public String fullName() {
            return javaFakerName.name();
        }

        @Programmatic
        public String firstName() {
            return javaFakerName.firstName();
        }

        @Programmatic
        public String lastName() {
            return javaFakerName.lastName();
        }

        @Programmatic
        public String prefix() {
            return javaFakerName.prefix();
        }

        @Programmatic
        public String suffix() {
            return javaFakerName.suffix();
        }
    }

    public class Comms {
        com.github.javafaker.Internet javaFakerInternet;
        com.github.javafaker.PhoneNumber javaFakerPhoneNumber;

        Comms(final com.github.javafaker.Name name, final FakeValuesService fakeValuesService) {
            javaFakerInternet = new com.github.javafaker.Internet(name, fakeValuesService);
            javaFakerPhoneNumber = new com.github.javafaker.PhoneNumber(fakeValuesService);
        }

        @Programmatic
        public String emailAddress() {
            return javaFakerInternet.emailAddress();
        }

        @Programmatic
        public String url() {
            return javaFakerInternet.url();
        }

        @Programmatic
        public String phoneNumber() {
            return javaFakerPhoneNumber.phoneNumber();
        }

    }

    public class Lorem {
        com.github.javafaker.Lorem javaFakerLorem;
        Lorem(final FakeValuesService fakeValuesService, final RandomService randomService) {
            javaFakerLorem = new com.github.javafaker.Lorem(fakeValuesService, randomService);
        }

        @Programmatic
        public List<String> words(int num) {
            return javaFakerLorem.words(num);
        }

        @Programmatic
        public List<String> words() {
            return javaFakerLorem.words();
        }

        @Programmatic
        public String sentence(int wordCount) {
            return javaFakerLorem.sentence(wordCount);
        }

        @Programmatic
        public String sentence() {
            return javaFakerLorem.sentence();
        }

        @Programmatic
        public String paragraph(int sentenceCount) {
            return javaFakerLorem.paragraph(sentenceCount);
        }

        @Programmatic
        public String paragraph() {
            return javaFakerLorem.paragraph();
        }

        @Programmatic
        public List<String> paragraphs(int paragraphCount) {
            return javaFakerLorem.paragraphs(paragraphCount);
        }
    }

    public class Address {
        com.github.javafaker.Address javaFakerAddress;
        Address(final com.github.javafaker.Name name, final FakeValuesService fakeValuesService, final RandomService randomService) {
            javaFakerAddress = new com.github.javafaker.Address(name, fakeValuesService, randomService);
        }

        @Programmatic
        public String streetName() {
            return javaFakerAddress.streetName();
        }

        @Programmatic
        public String streetAddressNumber() {
            return javaFakerAddress.streetAddressNumber();
        }

        @Programmatic
        public String streetAddress() {
            return javaFakerAddress.streetAddress(false);
        }

        @Programmatic
        public String streetAddressWithSecondary() {
            return javaFakerAddress.streetAddress(true);
        }

        @Programmatic
        public String usZipCode() {
            return javaFakerAddress.zipCode();
        }

        @Programmatic
        public String streetSuffix() {
            return javaFakerAddress.streetSuffix();
        }

        @Programmatic
        public String citySuffix() {
            return javaFakerAddress.citySuffix();
        }

        @Programmatic
        public String cityPrefix() {
            return javaFakerAddress.cityPrefix();
        }

        @Programmatic
        public String city() {
            return cityPrefix() + " " + name.firstName() + " " + citySuffix();
        }

        @Programmatic
        public String usStateAbbr() {
            return javaFakerAddress.stateAbbr();
        }

        @Programmatic
        public String country() {
            return javaFakerAddress.country();
        }

        @Programmatic
        public String latitude() {
            return javaFakerAddress.latitude();
        }

        @Programmatic
        public String longitude() {
            return javaFakerAddress.longitude();
        }

    }

    public class CreditCard {
        com.github.javafaker.Business javaFakerBusiness;
        CreditCard(final FakeValuesService fakeValuesService) {
            javaFakerBusiness = new com.github.javafaker.Business(fakeValuesService);
        }

        @Programmatic
        public String number() {
            return fakeValuesService.fetchString("business.credit_card_numbers");
        }

        @Programmatic
        public String type() {
            return fakeValuesService.fetchString("business.credit_card_types");
        }

    }

    public class Book {
        com.github.javafaker.Code javaFakerCode;
        Book(final RandomService randomService) {
            javaFakerCode = new com.github.javafaker.Code(randomService);
        }

        @Programmatic
        public String isbn10() {
            return javaFakerCode.isbn10();
        }

        @Programmatic
        public String isbn13() {
            return javaFakerCode.isbn13();
        }
    }

    public class Strings {

        @Programmatic
        public String upper(final int numChars) {
            final StringBuilder buf = new StringBuilder();
            for (int i = 0; i < numChars; i++) {
                buf.append(chars().upper());
            }
            return buf.toString();
        }

        @Programmatic
        public String fixed(int numChars) {
            return lorem.javaFakerLorem.fixedString(numChars);
        }

    }

    public class Bytes {

        @Programmatic
        public byte upTo(final byte upTo) {
            return (byte) ints().upTo(upTo);
        }

        @Programmatic
        public byte between(final byte min, final byte max) {
            return (byte) ints().between(min, max);
        }

        public byte any() {
            return (byte) ints().any();
        }
    }

    public class Shorts {

        @Programmatic
        public short upTo(final short upTo) {
            return (short) ints().upTo(upTo);
        }

        @Programmatic
        public short between(final short min, final short max) {
            return (short) ints().between(min, max);
        }

        public short any() {
            return (short) ints().any();
        }
    }

    public class Integers {

        @Programmatic
        public int upTo(final int upTo) {
            return randomService.nextInt(upTo);
        }

        @Programmatic
        public int between(final int min, final int max) {
            return min + randomService.nextInt(max-min);
        }

        public int any() {
            return RandomUtils.nextInt();
        }
    }

    public class Longs {

        public long any() {
            return RandomUtils.nextLong(random);
        }
    }

    public class Floats {

        public float any() {
            return RandomUtils.nextFloat(random);
        }
    }

    public class Doubles {

        public double any() {
            return RandomUtils.nextDouble(random);
        }
    }

    public class Chars {

        @Programmatic
        public char upper() {
            final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            return collections().randomElement(chars);
        }

        public char any() {
            final int any = shorts().any();
            final int i = any - Short.MIN_VALUE;
            return (char)i;
        }
    }

    public class Booleans {

        @Programmatic
        public boolean coinFlip() {
            return randomService.nextDouble() < 0.5;
        }

        @Programmatic
        public boolean diceRollOf6() {
            return ints().upTo(6) == 5;
        }
    }


    public class Collections  {

        @Programmatic
        public <E extends Enum<E>> E randomEnum(final Class<E> enumType) {
            final E[] enumConstants = enumType.getEnumConstants();
            return enumConstants[ints().upTo(enumConstants.length)];
        }

        @Programmatic
        public <T> T randomBounded(final Class<T> cls) {
            final List<T> list = container.allInstances(cls);
            return randomElement(list);
        }

        @Programmatic
        public <T> T randomElement(final List<T> list) {
            final int randomIdx = ints().upTo(list.size());
            return list.get(randomIdx);
        }

        @Programmatic
        public char randomElement(final char[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public byte randomElement(final byte[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public short randomElement(final short[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public int randomElement(final int[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public long randomElement(final long[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public float randomElement(final float[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public double randomElement(final double[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public boolean randomElement(final boolean[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }

        @Programmatic
        public <T> T randomElement(final T[] elements) {
            final int randomIdx = ints().upTo(elements.length);
            return elements[randomIdx];
        }
    }


    public class LocalDates {

        @Programmatic
        public LocalDate around(final Period period) {
            final LocalDate now = clockService.now();
            return booleans().coinFlip() ? before(period): after(period);
        }

        @Programmatic
        public LocalDate before(final Period period) {
            final LocalDate now = clockService.now();
            return now.minus(period);
        }

        @Programmatic
        public LocalDate after(final Period period) {
            final LocalDate now = clockService.now();
            return now.plus(period);
        }

    }

    public class Periods {

        @Programmatic
        public Period daysBetween(final int minDays, final int maxDays) {
            return Period.days(ints().between(minDays, maxDays));
        }

        @Programmatic
        public Period daysUpTo(final int maxDays) {
            return daysBetween(0, maxDays);
        }

        @Programmatic
        public Period monthsBetween(final int minMonths, final int maxMonths) {
            return Period.months(ints().between(minMonths, maxMonths));
        }

        @Programmatic
        public Period monthsUpTo(final int months) {
            return monthsBetween(0, months);
        }

        @Programmatic
        public Period yearsBetween(final int minYears, final int maxYears) {
            return Period.years(ints().between(minYears, maxYears));
        }

        @Programmatic
        public Period yearsUpTo(final int years) {
            return yearsBetween(0, years);
        }

    }

    // //////////////////////////////////////

    @Inject
    private ClockService clockService;

    @Inject
    private DomainObjectContainer container;


}