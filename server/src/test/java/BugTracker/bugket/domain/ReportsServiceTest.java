package BugTracker.bugket.domain;

import BugTracker.bugket.data.ReportRepository;
import BugTracker.bugket.models.Report;
import BugTracker.bugket.data.ReportRepositoryDouble;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportsServiceTest {

    ReportRepository repository = new ReportRepositoryDouble();
    ReportsService service = new ReportsService(repository);

    @Test
    void shouldFindAll() {
        List<Report> expected = repository.findAll();
        List<Report> actual = service.getAll();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void shouldFindIncomplete() {
        List<Report> expected = repository.findIncomplete();
        List<Report> actual = service.getAllIncomplete();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void shouldFindByUsername() {
        List<Report> expected = repository.findByUsername("test");
        List<Report> actual = service.getByUsername("test");
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void shouldCreate() {
        Report expected = new Report(
                "Ate my homework", "The program deleted my file", "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(expected);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotCreateNull() {
        Report report = new Report(
                null, null, null,
                null, false, null
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreateWithNullTitle() {
        Report report = new Report(
                null, "The program deleted my file", "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotCreateWithNullIssueDescription() {
        Report report = new Report(
                "Ate my homework", null, "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }
    @Test
    void shouldNotCreateWithNullReplicationInstructions() {
        Report report = new Report(
                "Ate my homework", "The program deleted my file", null,
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }
    @Test
    void shouldNotCreateWithNullAuthor() {
        Report report = new Report(
                "Ate my homework", "The program deleted my file", "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, null
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotCreateWithInvalidTitle() {
        Report report = new Report (
                "This paragraph is over 100 characters long and isn't valid. This paragraph is over 100 characters long and isn't valid.",
                "The program deleted my file", "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotCreateWithInvalidIssueDescription() {
        Report report = new Report(
                "Test", """
                Section 3
                The Senate of the United States shall be composed of two Senators from each State, chosen by the 
                Legislature thereof, for six Years; and each Senator shall have one Vote.
                                        
                Immediately after they shall be assembled in Consequence of the first Election, they shall be divided 
                as equally as may be into three Classes. The Seats of the Senators of the first Class shall be vacated 
                at the Expiration of the second Year, of the second Class at the Expiration of the fourth Year, and of 
                the third Class at the Expiration of the sixth Year, so that one third may be chosen every second Year; 
                and if Vacancies happen by Resignation, or otherwise, during the Recess of the Legislature of any State, 
                the Executive thereof may make temporary Appointments until the next Meeting of the Legislature, which 
                shall then fill such Vacancies.
                                        
                No Person shall be a Senator who shall not have attained to the Age of thirty Years, and been nine Years 
                a Citizen of the United States, and who shall not, when elected, be an Inhabitant of that State for 
                which he shall be chosen.
                                        
                The Vice President of the United States shall be President of the Senate, but shall have no Vote, unless 
                they be equally divided.
                                        
                The Senate shall chuse their other Officers, and also a President pro tempore, in the Absence of the 
                Vice President, or when he shall exercise the Office of President of the United States.
                                        
                The Senate shall have the sole Power to try all Impeachments. When sitting for that Purpose, they shall 
                be on Oath or Affirmation. When the President of the United States is tried, the Chief Justice shall 
                preside: And no Person shall be convicted without the Concurrence of two thirds of the Members present.
                                        
                Judgment in Cases of Impeachment shall not extend further than to removal from Office, and 
                disqualification to hold and enjoy any Office of honor, Trust or Profit under the United States: but the 
                Party convicted shall nevertheless be liable and subject to Indictment, Trial, Judgment and Punishment, 
                according to Law.
                                        
                Section 4
                The Times, Places and Manner of holding Elections for Senators and Representatives, shall be prescribed 
                in each State by the Legislature thereof; but the Congress may at any time by Law make or alter such 
                Regulations, except as to the Places of chusing Senators.
                                        
                The Congress shall assemble at least once in every Year, and such Meeting shall be on the first Monday 
                in December, unless they shall by Law appoint a different Day.
                                        
                Section 5
                Each House shall be the Judge of the Elections, Returns and Qualifications of its own Members, and a 
                Majority of each shall constitute a Quorum to do Business; but a smaller Number may adjourn from day to 
                day, and may be authorized to compel the Attendance of absent Members, in such Manner, and under such 
                Penalties as each House may provide.
                                        
                Each House may determine the Rules of its Proceedings, punish its Members for disorderly Behaviour, and, 
                with the Concurrence of two thirds, expel a Member.
                                        
                Each House shall keep a Journal of its Proceedings, and from time to time publish the same, excepting 
                such Parts as may in their Judgment require Secrecy; and the Yeas and Nays of the Members of either 
                House on any question shall, at the Desire of one fifth of those Present, be entered on the Journal.
                                        
                Neither House, during the Session of Congress, shall, without the Consent of the other, adjourn for more 
                than three days, nor to any other Place than that in which the two Houses shall be sitting.
                        """,
                "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotAddWithInvalidReplicationInstructions() {
        Report report = new Report(
                "Test", "The program deleted my file", """
                We the People of the United States, in Order to form a more perfect Union, establish Justice, 
                insure domestic Tranquility, provide for the common defence, promote the general Welfare, and 
                secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish this Constitution 
                for the United States of America.
                Article I
                Section 1
                All legislative Powers herein granted shall be vested in a Congress of the United States, which shall 
                consist of a Senate and House of Representatives.
                                
                Section 2
                The House of Representatives shall be composed of Members chosen every second Year by the People of the 
                several States, and the Electors in each State shall have the Qualifications requisite for Electors of 
                the most numerous Branch of the State Legislature.
                                
                No Person shall be a Representative who shall not have attained to the Age of twenty five Years, and 
                been seven Years a Citizen of the United States, and who shall not, when elected, be an Inhabitant of 
                that State in which he shall be chosen.
                                
                Representatives and direct Taxes shall be apportioned among the several States which may be included 
                within this Union, according to their respective Numbers, which shall be determined by adding to the 
                whole Number of free Persons, including those bound to Service for a Term of Years, and excluding 
                Indians not taxed, three fifths of all other Persons. The actual Enumeration shall be made within three 
                Years after the first Meeting of the Congress of the United States, and within every subsequent Term of 
                ten Years, in such Manner as they shall by Law direct. The Number of Representatives shall not exceed 
                one for every thirty Thousand, but each State shall have at Least one Representative; and until such 
                enumeration shall be made, the State of New Hampshire shall be entitled to chuse three, Massachusetts 
                eight, Rhode Island and Providence Plantations one, Connecticut five, New-York six, New Jersey four, 
                Pennsylvania eight, Delaware one, Maryland six, Virginia ten, North Carolina five, South Carolina five, 
                and Georgia three.
                                
                When vacancies happen in the Representation from any State, the Executive Authority thereof shall issue 
                Writs of Election to fill such Vacancies.
                                
                The House of Representatives shall chuse their Speaker and other Officers; and shall have the sole Power 
                of Impeachment.
                """,

                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotAddSpamText() {
        Report report = new Report(
                "a asdifo@kenr2udhewBjeisSarud)wns.lfes\"lrod", "The program deleted my file", "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result = service.add(report);
        assertFalse(result.isSuccess());

        Report report2 = new Report(
                "Ate my homework", "The program deleted my file", "a asdifo@kenr2udhewBjeisSarud)wns.lfes\"lrod",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result2 = service.add(report);
        assertFalse(result2.isSuccess());

        Report report3 = new Report(
                "Ate my homework", """
                a asdifo@kenr2udhewBjeisSarud)wns.lfes"lrod
                """, "Save a Word document at any time",
                LocalDate.of(2022, 03, 13), false, "test"
        );
        Result<Report> result3 = service.add(report);
        assertFalse(result3.isSuccess());
    }

    @Test
    void shouldFindByVote() {
        List<Report> expected = repository.findByVote("test");
        List<Report> actual = service.getByVote("test");
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void shouldUpdate() {
        boolean expected = repository.updateStatus(1, true);
        Result actual = service.updateStatus(1, true);
        assertEquals(expected, actual.isSuccess());
    }

    @Test
    void shouldNotUpdateWithInvalidId() {
        Result result = service.updateStatus(0, true);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

}