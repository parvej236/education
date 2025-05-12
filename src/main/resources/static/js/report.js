function generateApplicantListPdfReport(reportData) {
    const title = reportData.header.title.trim();
    const pdf = new PDF(title, reportData.header.subTitle, 'landscape');
    const header = [
        {text: 'SL', alignment: 'center'},
        {text: 'Image', alignment: 'center'},
        {text: 'Applicant Info', alignment: 'center'},
        {text: 'Academic Info', alignment: 'center'},
        {text: 'Address', alignment: 'center'}
    ];
    const rows = [];
    const data = reportData.result['data'];
    const filename = title.replace(/\s+/g, '_').toLowerCase();

    for (const [index, applicant] of data.entries()) {
        let row = [
            {text: index + 1, alignment: 'center', margin: [0, 25, 0, 25]},
            applicant.base64 !== null ? {image: applicant.base64, fit: [60, 60],} : {},
            {
                text: [
                    {text: 'Name: '},
                    {text: applicant.nameEn, bold: true},
                    {text: '\nGender: '},
                    {text: applicant.gender, bold: true},
                    {text: '\nReligion: '},
                    {text: applicant.religion, bold: true},
                    {text: '\nCommunity: '},
                    {text: applicant.community, bold: true}
                ]
            },
            {
                text: [
                    {text: 'Form No.: '},
                    {text: applicant.formNo, bold: true},
                    {text: '\nClass: '},
                    {text: applicant.appliedClass, bold: true},
                    {text: '\nSession: '},
                    {text: applicant.session, bold: true},
                    {text: '\nCenter: '},
                    {text: applicant.examCenter, bold: true}
                ]
            },
            {
                text: [
                    {text: 'Address: '},
                    {text: applicant.presentAddress, font: window.env.fonts.bn, bold: true},
                    {text: '\nUnion: '},
                    {text: applicant.presentUnion, font: window.env.fonts.bn, bold: true},
                    {text: '\nThana: '},
                    {text: applicant.presentUpazila, font: window.env.fonts.bn, bold: true},
                    {text: '\nDistrict: '},
                    {text: applicant.presentDistrict, font: window.env.fonts.bn, bold: true}
                ]
            },
        ];
        rows.push(row);
    }

    pdf.addTable(header, rows, ['auto', 'auto', 'auto', 'auto', 'auto']);
    pdf.downloadPdf(filename);
}

function generateApplicantListCsvReport(reportData) {
    const title = reportData.header.title.trim();
    const csv = new CSV(title, reportData.header.subTitle);
    const header = ['SL', 'Form No.', 'Applied Class', 'Session', 'Exam Center', 'Apply Date', 'Name English', 'Name Bangla', 'Gender', 'Community',
        'Religion', 'Date of Birth', 'Age', 'Birth Certificate', 'Address', 'Union', 'Thana', 'District', 'Country', 'Selected', 'Admitted', 'Comments'];
    const rows = [];
    let data = reportData.result['data'];
    const filename = title.replace(/\s+/g, '_').toLowerCase().concat('.csv');

    for (const [index, applicant] of data.entries()) {
        let row = [
            index + 1,
            applicant.formNo,
            applicant.appliedClass,
            applicant.session,
            applicant.examCenter,
            applicant.applyDate,
            applicant.nameEn,
            applicant.nameBn,
            applicant.gender,
            applicant.community,
            applicant.religion,
            applicant.birthDate,
            applicant.age,
            applicant.birthCertificateNumber,
            applicant.presentAddress,
            applicant.presentUnion,
            applicant.presentUpazila,
            applicant.presentDistrict,
            applicant.presentCountry,
            applicant.selected,
            applicant.admitted,
            applicant.comment,
        ];
        rows.push(row);
    }

    csv.addTable(header, rows);
    csv.downloadCsv(filename);
}

function generateStudentListPdfReport(reportData) {
    const title = reportData.header.title.trim();
    const pdf = new PDF(title, reportData.header.subTitle, 'landscape');
    const header = [
        {text: 'SL', alignment: 'center'},
        {text: 'Image', alignment: 'center'},
        {text: 'Student Info', alignment: 'center'},
        {text: 'Academic Info', alignment: 'center'}
    ];
    const rows = [];
    const data = reportData.result['data'];
    const filename = title.replace(/\s+/g, '_').toLowerCase();

    for (const [index, student] of data.entries()) {
        let row = [
            {text: index + 1, alignment: 'center', margin: [0, 25, 0, 25]},
            student.base64 !== null ? {image: student.base64, fit: [60, 60],} : {},
            {
                text: [
                    {text: 'Name: '},
                    {text: student.nameEn, bold: true},
                    {text: '\nGender: '},
                    {text: student.gender, bold: true},
                    {text: '\nCommunity: '},
                    {text: student.community, bold: true},
                    {text: '\nReligion: '},
                    {text: student.religion, bold: true},
                    {text: '\nDate of Birth: '},
                    {text: student.dob, bold: true}
                ]
            },
            {
                text: [
                    {text: 'Quantaa ID: '},
                    {text: student.studentId, bold: true},
                    {text: '\nClass: '},
                    {text: student.cls, bold: true},
                    {text: '\nInstitution: '},
                    {text: student.institution, bold: true},
                    {text: '\nResidence: '},
                    {text: student.residence, bold: true},
                    {text: '\nStatus: '},
                    {text: student.status, bold: true}
                ]
            }
        ];
        rows.push(row);
    }

    pdf.addTable(header, rows, ['auto', 'auto', 'auto', 'auto']);
    pdf.downloadPdf(filename);
}

function generateStudentListCsvReport(reportData) {
    const title = reportData.header.title.trim();
    const csv = new CSV(title, reportData.header.subTitle);
    const header = ['SL', 'Name', 'Quantaa ID', 'Gender', 'Community', 'Religion', 'Date of Birth', 'Class', 'Institution', 'Residence', 'Status'];
    const rows = [];
    let data = reportData.result['data'];
    const filename = title.replace(/\s+/g, '_').toLowerCase().concat('.csv');

    for (const [index, student] of data.entries()) {
        let row = [
            index + 1,
            student.nameEn,
            student.studentId,
            student.gender,
            student.community,
            student.religion,
            student.dob,
            student.cls,
            student.institution,
            student.residence,
            student.status
        ];
        rows.push(row);
    }

    csv.addTable(header, rows);
    csv.downloadCsv(filename);
}