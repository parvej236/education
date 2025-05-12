INSERT INTO education.adm_exam_center(id, created_at, created_by, updated_by, updated_at, name, code, district, active, responsible, phone, email, address, remarks)
SELECT id, created,
       CASE
           WHEN created_by IS NULL THEN 1
           ELSE created_by
       END AS created_by,
       CASE
           WHEN last_updated_by IS NULL THEN 1
           ELSE last_updated_by
       END AS last_updated_by,
       updated, name, code, district,
       CASE
           WHEN status = 1 THEN true
           WHEN status = 2 THEN false
       END AS exam_center_status,
       responsible_person, phone, email, address, remarks
FROM qeis.adm_exam_center;

INSERT INTO education.adm_document(id, created_at, created_by, updated_by, updated_at, document_name,  document_code, remarks)
SELECT id, created, created_by, last_updated_by, updated, document_name, document_code, remarks
FROM qeis.adm_documents_information;

INSERT INTO education.aca_class(id, created_at, created_by, updated_by, updated_at, name, level, remarks)
SELECT id, created, created_by,
       CASE
           WHEN last_updated_by IS NULL THEN 1
           ELSE last_updated_by
       END AS last_updated_by,
       updated, name, level, remarks
FROM qeis.academic_class;

INSERT INTO education.aca_subject(id, created_at, created_by, updated_by, updated_at, name, code, status, remarks)
SELECT id,created, created_by,last_updated_by,updated, name, code, status, remarks
FROM qeis.academic_subject;

INSERT INTO education.aca_subject_type(id, created_at, created_by, updated_by, updated_at, name, remarks, status)
SELECT id, created, created_by, last_updated_by, updated, name, remarks, status
FROM qeis.academic_subject_type;

INSERT INTO education.aca_exam(id, created_at, created_by, updated_by, updated_at, name, status, remarks)
SELECT id, created, created_by, last_updated_by, updated, name, status, remarks
FROM qeis.academic_exam;

INSERT INTO education.aca_institution(id, created_at, created_by, updated_by, updated_at, name, code, phone, email, responsible_person, address, remarks)
SELECT id, created, created_by, last_updated_by, updated, name, code, phone, email, responsible_person, address, remarks
FROM qeis.institution;

INSERT INTO education.aca_institution_class(created_at, created_by, updated_at, updated_by, class_id, institution_id)
SELECT NOW(), 1, NOW(), 1, clazz_id, institution_id
FROM qeis.institution_clazz;

INSERT INTO education.adm_applicant(id, created_at, created_by, updated_by, updated_at, form_no, applied_class,
                                          session, exam_center, apply_date, name_en, name_bn, religion, community,
                                          gender, birth_date, birth_certificate_number, present_country,
                                          present_district, present_upazila, present_union, present_address,
                                          permanent_country, permanent_district, permanent_upazila, permanent_union,
                                          permanent_address, selected, admitted, comment, image_path)
SELECT id, created, created_by, last_updated_by, updated, form_no, expected_class,
       session, exam_center, apply_date, name_en, name_bn, religion, community, gender, date_of_birth,
       birth_certificate, present_country, present_district, present_upazila, present_union, present_address,
       permanent_country, permanent_district, permanent_upazila, permanent_union, permanent_address, selected_app,
       admitted_app, applicant_comments, applicant_image_path
FROM qeis.adm_applicant;

UPDATE education.adm_applicant AS a
SET applied_class = s.name
FROM education.aca_class AS s
where cast(s.id as varchar) = a.applied_class
  AND a.applied_class is not null;

UPDATE education.adm_applicant AS a
SET exam_center = s.name
FROM education.adm_exam_center AS s
where cast(s.id as varchar) = a.exam_center
  AND a.exam_center is not null;

UPDATE education.adm_applicant AS a
SET religion = s.description
FROM qeis.common_data AS s
where cast(s.code as varchar) = a.religion
  AND a.religion is not null
  and s.field_key = 'Religion';

UPDATE education.adm_applicant AS a
SET community = s.description
FROM qeis.common_data AS s
where cast(s.code as varchar) = a.community
  AND a.community is not null
  and s.field_key = 'Community';

UPDATE education.adm_applicant
set gender = 'Male'
where gender = '1';

UPDATE education.adm_applicant
set gender = 'Female'
where gender = '2';

UPDATE education.adm_applicant SET present_district = NULL WHERE present_district = '';
UPDATE education.adm_applicant SET present_upazila = NULL WHERE present_upazila = '';
UPDATE education.adm_applicant SET permanent_district = NULL WHERE permanent_district = '';
UPDATE education.adm_applicant SET permanent_upazila = NULL WHERE permanent_upazila = '';

UPDATE education.adm_applicant AS e
SET present_address = CASE
      WHEN q.present_village = '' AND q.present_mouja = '' AND q.present_union = ''
          AND q.present_post_office = '' AND q.present_post_code = ''
          AND q.present_ward = '' AND q.present_thana = ''
          THEN NULL

      ELSE CONCAT_WS(', ',
         NULLIF(q.present_village, ''),
         NULLIF(q.present_mouja, ''),
         NULLIF(q.present_union, ''),
         NULLIF(q.present_post_office, ''),
         CASE
             WHEN q.present_post_code != ''
                 THEN CONCAT(q.present_post_office, ' - ', q.present_post_code)
             ELSE NULL END,
         CASE
             WHEN q.present_ward != '' THEN q.present_ward
             ELSE NULL END,
         NULLIF(q.present_thana, '')
      )
    END
FROM qeis.adm_applicant AS q
WHERE e.id = q.id;

UPDATE education.adm_applicant AS e
SET permanent_address = CASE
      WHEN q.permanent_village = '' AND q.permanent_mouja = '' AND q.permanent_union = ''
          AND q.permanent_post_office = '' AND q.permanent_post_code = ''
          AND q.permanent_ward = '' AND q.permanent_thana = ''
          THEN NULL

      ELSE CONCAT_WS(', ',
         NULLIF(q.permanent_village, ''),
         NULLIF(q.permanent_mouja, ''),
         NULLIF(q.permanent_union, ''),
         NULLIF(q.permanent_post_office, ''),
         CASE
             WHEN q.permanent_post_code != ''
                 THEN CONCAT(q.permanent_post_office, ' - ', q.permanent_post_code)
             ELSE NULL END,
         CASE
             WHEN q.permanent_ward != '' THEN q.permanent_ward
             ELSE NULL END,
         NULLIF(q.permanent_thana, '')
      )
    END
FROM qeis.adm_applicant AS q
WHERE e.id = q.id;

UPDATE education.adm_applicant
SET present_union = NULL,
    permanent_union = NULL,
    present_country = 'Bangladesh',
    permanent_country = 'Bangladesh';

-- Present District

UPDATE education.adm_applicant SET present_district = 'Bagerhat'
WHERE present_district LIKE '%বাগের%';

UPDATE education.adm_applicant SET present_district = 'Bandarban'
WHERE present_district LIKE '%বান্দ%'
   OR present_district LIKE '%বন্দর%'
   OR present_district LIKE '%বিন্দর%'
   OR present_district LIKE '%বানদর%'
   OR present_district LIKE '%ব্ন্%'
   OR present_district LIKE '%বাংলাদেশ%'
   OR present_district LIKE '%চকরিয়া%'
   OR present_district LIKE '%BANDARBAN%'
   OR present_district LIKE '%আলীকদম%'
   OR present_district LIKE '%লামা%'
   OR present_district LIKE '%রুমা%'
   OR present_district LIKE '%রোয়াংছড়ি%'
   OR present_district LIKE '%নাইক্ষ্যংছড়ি%'
   OR present_district LIKE '%থানছি%'
   OR present_district LIKE '%বা্ন্দর%';

UPDATE education.adm_applicant SET present_district = 'Barguna'
WHERE present_district LIKE '%বরগুনা%';

UPDATE education.adm_applicant SET present_district = 'Barisal'
WHERE present_district LIKE '%বরিশা%'
   OR present_district LIKE '%রবিশাল%';

UPDATE education.adm_applicant SET present_district = 'Bhola'
WHERE present_district LIKE '%ভোলা%'
   OR present_district LIKE '%ভোলা%';

UPDATE education.adm_applicant SET present_district = 'Bogura'
WHERE present_district LIKE '%বগুড়া%';

UPDATE education.adm_applicant SET present_district = 'Brahmanbaria'
WHERE present_district LIKE '%ব্র%'
   OR present_district LIKE '%বি-বাড়িয়া%';

UPDATE education.adm_applicant SET present_district = 'Chandpur'
WHERE present_district LIKE '%চা%র%';

UPDATE education.adm_applicant SET present_district = 'Chapainawabganj'
WHERE present_district LIKE '%চাঁপাই%'
   OR present_district LIKE '%চাপাই%';

UPDATE education.adm_applicant SET present_district = 'Chattogram'
WHERE present_district LIKE '%চ%গ্রাম%'
   OR present_district LIKE '%লােহাগাড়া%'
   OR present_district LIKE '%লোহাগড়া%'
   OR present_district LIKE '%কর্ণফুলি%';

UPDATE education.adm_applicant SET present_district = 'Chuadanga'
WHERE present_district LIKE '%চুয়া%';

UPDATE education.adm_applicant SET present_district = 'Cumilla'
WHERE present_district LIKE '%কুমিল্লা%';

UPDATE education.adm_applicant SET present_district = 'Coxsbazar'
WHERE present_district LIKE '%কক্সবাজার%'
   OR present_district LIKE '%ককাবাজার%';

UPDATE education.adm_applicant SET present_district = 'Dhaka'
WHERE present_district LIKE '%ঢাকা%'
   OR present_district LIKE '%ঢ়াকা%';

UPDATE education.adm_applicant SET present_district = 'Dinajpur'
WHERE present_district LIKE '%দিনাজ%'
   OR present_district LIKE '%বিরামপুর%';

UPDATE education.adm_applicant SET present_district = 'Faridpur'
WHERE present_district LIKE '%ফরিদপুর%';

UPDATE education.adm_applicant SET present_district = 'Feni'
WHERE present_district LIKE '%ফে%';

UPDATE education.adm_applicant SET present_district = 'Gaibandha'
WHERE present_district LIKE '%গাইবান্ধা%';

UPDATE education.adm_applicant SET present_district = 'Gazipur'
WHERE present_district LIKE '%গাজীপুর%';

UPDATE education.adm_applicant SET present_district = 'Gopalganj'
WHERE present_district LIKE '%গোপাল%';

UPDATE education.adm_applicant SET present_district = 'Habiganj'
WHERE present_district LIKE '%হবি%';

UPDATE education.adm_applicant SET present_district = 'Jamalpur'
WHERE present_district LIKE '%জামা%';

UPDATE education.adm_applicant SET present_district = 'Jashore'
WHERE present_district LIKE '%যশ%'
   OR present_district LIKE '%যোশর%';

UPDATE education.adm_applicant SET present_district = 'Jhalakathi'
WHERE present_district LIKE '%ঝাল%';

UPDATE education.adm_applicant SET present_district = 'Jhenaidah'
WHERE present_district LIKE '%ঝিনাইদহ%';

UPDATE education.adm_applicant SET present_district = 'Joypurhat'
WHERE present_district LIKE '%জয়%';

UPDATE education.adm_applicant SET present_district = 'Khagrachhari'
WHERE present_district LIKE '%খা%ছড়ি%'
   OR present_district LIKE '%খা%ড়%'
   OR present_district LIKE '%ঘাগড়াছড়ি%'
   OR present_district LIKE '%খাগড়াছাড়ি%'
   OR present_district LIKE '%লক্ষীছড়ি%';

UPDATE education.adm_applicant SET present_district = 'Khulna'
WHERE present_district LIKE '%খু%';

UPDATE education.adm_applicant SET present_district = 'Kishoreganj'
WHERE present_district LIKE '%কিশোরগঞ্জ%'
   OR present_district LIKE '%কিশোরগঞ্জ%';

UPDATE education.adm_applicant SET present_district = 'Kurigram'
WHERE present_district LIKE '%কুড়িগ্রাম%';

UPDATE education.adm_applicant SET present_district = 'Kushtia'
WHERE present_district LIKE '%কুষ্টিয়া%';

UPDATE education.adm_applicant SET present_district = 'Lakshmipur'
WHERE present_district LIKE '%লক্ষী%'
   OR present_district LIKE '%লক্ষ্মী%';

UPDATE education.adm_applicant SET present_district = 'Lalmonirhat'
WHERE present_district LIKE '%লাল%';

UPDATE education.adm_applicant SET present_district = 'Madaripur'
WHERE present_district LIKE '%মাদার%';

UPDATE education.adm_applicant SET present_district = 'Magura'
WHERE present_district LIKE '%মাগু%';

UPDATE education.adm_applicant SET present_district = 'Manikganj'
WHERE present_district LIKE '%মানিকগঞ্জ%';

UPDATE education.adm_applicant SET present_district = 'Meherpur'
WHERE present_district LIKE '%মে%';

UPDATE education.adm_applicant SET present_district = 'Moulvibazar'
WHERE present_district LIKE '%মৌ%';

UPDATE education.adm_applicant SET present_district = 'Munshiganj'
WHERE present_district LIKE '%মু%';

UPDATE education.adm_applicant SET present_district = 'Mymensingh'
WHERE present_district LIKE '%ময়%';

UPDATE education.adm_applicant SET present_district = 'Naogaon'
WHERE present_district LIKE '%নওগ%';

UPDATE education.adm_applicant SET present_district = 'Narail'
WHERE present_district LIKE '%নড়%';

UPDATE education.adm_applicant SET present_district = 'Narayanganj'
WHERE present_district LIKE '%নার%';

UPDATE education.adm_applicant SET present_district = 'Narsingdi'
WHERE present_district LIKE '%নর%';

UPDATE education.adm_applicant SET present_district = 'Natore'
WHERE present_district LIKE '%নাট%';

UPDATE education.adm_applicant SET present_district = 'Netrokona'
WHERE present_district LIKE '%নে%';

UPDATE education.adm_applicant SET present_district = 'Nilphamari'
WHERE present_district LIKE '%নীল%'
   OR present_district LIKE 'লীলফামারী';

UPDATE education.adm_applicant SET present_district = 'Noakhali'
WHERE present_district LIKE '%নো%'
   OR present_district LIKE '%নোয়াখালী%';

UPDATE education.adm_applicant SET present_district = 'Pabna'
WHERE present_district LIKE '%পাব%';

UPDATE education.adm_applicant SET present_district = 'Panchagarh'
WHERE present_district LIKE '%পঞ%';

UPDATE education.adm_applicant SET present_district = 'Patuakhali'
WHERE present_district LIKE '%পট%';

UPDATE education.adm_applicant SET present_district = 'Pirojpur'
WHERE present_district LIKE '%পি%';

UPDATE education.adm_applicant SET present_district = 'Rajbari'
WHERE present_district LIKE '%রাজবাড়ি%';

UPDATE education.adm_applicant SET present_district = 'Rajshahi'
WHERE present_district LIKE '%রাজ%';

UPDATE education.adm_applicant SET present_district = 'Rangamati'
WHERE present_district LIKE '%রাং%'
   OR present_district LIKE '%জুরা%'
   OR present_district LIKE '%নানিয়ারচর%'
   OR present_district LIKE '%রাা%'
   OR present_district LIKE '%দিঘ%'
   OR present_district LIKE '%বাঘাইছড়ি%'
   OR present_district LIKE '%কাপ্ত%'
   OR present_district LIKE '%বরক%'
   OR present_district LIKE '%রাঙ%';

UPDATE education.adm_applicant SET present_district = 'Rangpur'
WHERE present_district LIKE '%রং%';

UPDATE education.adm_applicant SET present_district = 'Satkhira'
WHERE present_district LIKE '%সা%';

UPDATE education.adm_applicant SET present_district = 'Shariatpur'
WHERE present_district LIKE '%শরী%';

UPDATE education.adm_applicant SET present_district = 'Sherpur'
WHERE present_district LIKE '%শের%';

UPDATE education.adm_applicant SET present_district = 'Sirajganj'
WHERE present_district LIKE '%সির%';

UPDATE education.adm_applicant SET present_district = 'Sunamganj'
WHERE present_district LIKE '%সু%';

UPDATE education.adm_applicant SET present_district = 'Sylhet'
WHERE present_district LIKE '%সিলে%';

UPDATE education.adm_applicant SET present_district = 'Tangail'
WHERE present_district LIKE '%টা%';

UPDATE education.adm_applicant SET present_district = 'Thakurgaon'
WHERE present_district LIKE '%ঠাকুরগাঁও%';

-- Permanent District

UPDATE education.adm_applicant SET permanent_district = 'Bagerhat'
WHERE permanent_district LIKE '%বাগের%';

UPDATE education.adm_applicant SET permanent_district = 'Bandarban'
WHERE permanent_district LIKE '%বান্দ%'
   OR permanent_district LIKE '%বন্দর%'
   OR permanent_district LIKE '%বিন্দর%'
   OR permanent_district LIKE '%বানদর%'
   OR permanent_district LIKE '%ব্ন্%'
   OR permanent_district LIKE '%বাংলাদেশ%'
   OR permanent_district LIKE '%চকরিয়া%'
   OR permanent_district LIKE '%BANDARBAN%'
   OR permanent_district LIKE '%আলীকদম%'
   OR permanent_district LIKE '%লামা%'
   OR permanent_district LIKE '%রুমা%'
   OR permanent_district LIKE '%রোয়াংছড়ি%'
   OR permanent_district LIKE '%নাইক্ষ্যংছড়ি%'
   OR permanent_district LIKE '%থানছি%'
   OR permanent_district LIKE '%বা্ন্দর%';

UPDATE education.adm_applicant SET permanent_district = 'Barguna'
WHERE permanent_district LIKE '%বরগুনা%';

UPDATE education.adm_applicant SET permanent_district = 'Barisal'
WHERE permanent_district LIKE '%বরিশা%'
   OR permanent_district LIKE '%রবিশাল%';

UPDATE education.adm_applicant SET permanent_district = 'Bhola'
WHERE permanent_district LIKE '%ভোলা%'
   OR permanent_district LIKE '%ভোলা%';

UPDATE education.adm_applicant SET permanent_district = 'Bogura'
WHERE permanent_district LIKE '%বগুড়া%';

UPDATE education.adm_applicant SET permanent_district = 'Brahmanbaria'
WHERE permanent_district LIKE '%ব্র%'
   OR permanent_district LIKE '%বি-বাড়িয়া%'
   OR permanent_district LIKE '%বি.%';

UPDATE education.adm_applicant SET permanent_district = 'Chandpur'
WHERE permanent_district LIKE '%চা%র%';

UPDATE education.adm_applicant SET permanent_district = 'Chapainawabganj'
WHERE permanent_district LIKE '%চাঁপাই%'
   OR permanent_district LIKE '%চাপাই%'
   OR permanent_district LIKE '%চাঁড়াইনবান%';

UPDATE education.adm_applicant SET permanent_district = 'Chattogram'
WHERE permanent_district LIKE '%চ%গ্রাম%'
   OR permanent_district LIKE '%লােহাগাড়া%'
   OR permanent_district LIKE '%লোহাগড়া%'
   OR permanent_district LIKE '%কর্ণফুলি%';

UPDATE education.adm_applicant SET permanent_district = 'Chuadanga'
WHERE permanent_district LIKE '%চুয়া%';

UPDATE education.adm_applicant SET permanent_district = 'Cumilla'
WHERE permanent_district LIKE '%কুমিল্লা%';

UPDATE education.adm_applicant SET permanent_district = 'Coxsbazar'
WHERE permanent_district LIKE '%কক্সবাজার%'
   OR permanent_district LIKE '%ককাবাজার%';

UPDATE education.adm_applicant SET permanent_district = 'Dhaka'
WHERE permanent_district LIKE '%ঢাকা%'
   OR permanent_district LIKE '%ঢ়াকা%';

UPDATE education.adm_applicant SET permanent_district = 'Dinajpur'
WHERE permanent_district LIKE '%দিনাজ%'
   OR permanent_district LIKE '%নবাবগঞ্জ%'
   OR permanent_district LIKE '%বিরামপুর%';

UPDATE education.adm_applicant SET permanent_district = 'Faridpur'
WHERE permanent_district LIKE '%ফরিদপুর%';

UPDATE education.adm_applicant SET permanent_district = 'Feni'
WHERE permanent_district LIKE '%ফে%';

UPDATE education.adm_applicant SET permanent_district = 'Gaibandha'
WHERE permanent_district LIKE '%গাইবান্ধা%';

UPDATE education.adm_applicant SET permanent_district = 'Gazipur'
WHERE permanent_district LIKE '%গাজীপুর%';

UPDATE education.adm_applicant SET permanent_district = 'Gopalganj'
WHERE permanent_district LIKE '%গোপাল%';

UPDATE education.adm_applicant SET permanent_district = 'Habiganj'
WHERE permanent_district LIKE '%হবি%';

UPDATE education.adm_applicant SET permanent_district = 'Jamalpur'
WHERE permanent_district LIKE '%জামা%';

UPDATE education.adm_applicant SET permanent_district = 'Jashore'
WHERE permanent_district LIKE '%যশ%'
   OR permanent_district LIKE '%যোশর%';

UPDATE education.adm_applicant SET permanent_district = 'Jhalakathi'
WHERE permanent_district LIKE '%ঝাল%';

UPDATE education.adm_applicant SET permanent_district = 'Jhenaidah'
WHERE permanent_district LIKE '%ঝিনাইদহ%';

UPDATE education.adm_applicant SET permanent_district = 'Joypurhat'
WHERE permanent_district LIKE '%জয়%';

UPDATE education.adm_applicant SET permanent_district = 'Khagrachhari'
WHERE permanent_district LIKE '%খা%ছড়ি%'
   OR permanent_district LIKE '%খা%ড়%'
   OR permanent_district LIKE '%ঘাগড়াছড়ি%'
   OR permanent_district LIKE '%খাগড়াছড়ী%'
   OR permanent_district LIKE '%খাগড়াছাড়ি%'
   OR permanent_district LIKE '%লক্ষীছড়ি%';

UPDATE education.adm_applicant SET permanent_district = 'Khulna'
WHERE permanent_district LIKE '%খু%';

UPDATE education.adm_applicant SET permanent_district = 'Kishoreganj'
WHERE permanent_district LIKE '%কিশোরগঞ্জ%'
   OR permanent_district LIKE '%কিশোরগঞ্জ%';

UPDATE education.adm_applicant SET permanent_district = 'Kurigram'
WHERE permanent_district LIKE '%কুড়িগ্রাম%';

UPDATE education.adm_applicant SET permanent_district = 'Kushtia'
WHERE permanent_district LIKE '%কুষ্টিয়া%';

UPDATE education.adm_applicant SET permanent_district = 'Lakshmipur'
WHERE permanent_district LIKE '%লক্ষী%'
   OR permanent_district LIKE '%লক্ষ্মী%';

UPDATE education.adm_applicant SET permanent_district = 'Lalmonirhat'
WHERE permanent_district LIKE '%লাল%';

UPDATE education.adm_applicant SET permanent_district = 'Madaripur'
WHERE permanent_district LIKE '%মাদার%';

UPDATE education.adm_applicant SET permanent_district = 'Magura'
WHERE permanent_district LIKE '%মাগু%';

UPDATE education.adm_applicant SET permanent_district = 'Manikganj'
WHERE permanent_district LIKE '%মানিকগঞ্জ%';

UPDATE education.adm_applicant SET permanent_district = 'Meherpur'
WHERE permanent_district LIKE '%মে%';

UPDATE education.adm_applicant SET permanent_district = 'Moulvibazar'
WHERE permanent_district LIKE '%মৌ%';

UPDATE education.adm_applicant SET permanent_district = 'Munshiganj'
WHERE permanent_district LIKE '%মু%';

UPDATE education.adm_applicant SET permanent_district = 'Mymensingh'
WHERE permanent_district LIKE '%ময়%';

UPDATE education.adm_applicant SET permanent_district = 'Naogaon'
WHERE permanent_district LIKE '%নওগ%';

UPDATE education.adm_applicant SET permanent_district = 'Narail'
WHERE permanent_district LIKE '%নড়%';

UPDATE education.adm_applicant SET permanent_district = 'Narayanganj'
WHERE permanent_district LIKE '%নার%';

UPDATE education.adm_applicant SET permanent_district = 'Narsingdi'
WHERE permanent_district LIKE '%নর%';

UPDATE education.adm_applicant SET permanent_district = 'Natore'
WHERE permanent_district LIKE '%নাট%';

UPDATE education.adm_applicant SET permanent_district = 'Netrokona'
WHERE permanent_district LIKE '%নে%';

UPDATE education.adm_applicant SET permanent_district = 'Nilphamari'
WHERE permanent_district LIKE '%নীল%'
   OR permanent_district LIKE 'লীলফামারী';

UPDATE education.adm_applicant SET permanent_district = 'Noakhali'
WHERE permanent_district LIKE '%নো%'
   OR permanent_district LIKE '%নোয়াখালী%'
   OR permanent_district LIKE '%হাতিয়া%';

UPDATE education.adm_applicant SET permanent_district = 'Pabna'
WHERE permanent_district LIKE '%পাব%';

UPDATE education.adm_applicant SET permanent_district = 'Panchagarh'
WHERE permanent_district LIKE '%পঞ%';

UPDATE education.adm_applicant SET permanent_district = 'Patuakhali'
WHERE permanent_district LIKE '%পট%';

UPDATE education.adm_applicant SET permanent_district = 'Pirojpur'
WHERE permanent_district LIKE '%কাউখালী%'
   OR permanent_district LIKE '%পি%';

UPDATE education.adm_applicant SET permanent_district = 'Rajbari'
WHERE permanent_district LIKE '%রাজবাড়ি%';

UPDATE education.adm_applicant SET permanent_district = 'Rajshahi'
WHERE permanent_district LIKE '%রাজ%';

UPDATE education.adm_applicant SET permanent_district = 'Rangamati'
WHERE permanent_district LIKE '%রাং%'
   OR permanent_district LIKE '%জুরা%'
   OR permanent_district LIKE '%নানিয়ারচর%'
   OR permanent_district LIKE '%রাা%'
   OR permanent_district LIKE '%দিঘ%'
   OR permanent_district LIKE '%বাঘাইছড়ি%'
   OR permanent_district LIKE '%কাপ্ত%'
   OR permanent_district LIKE '%বরক%'
   OR permanent_district LIKE '%রাঙ%';

UPDATE education.adm_applicant SET permanent_district = 'Rangpur'
WHERE permanent_district LIKE '%রং%';

UPDATE education.adm_applicant SET permanent_district = 'Satkhira'
WHERE permanent_district LIKE '%সা%';

UPDATE education.adm_applicant SET permanent_district = 'Shariatpur'
WHERE permanent_district LIKE '%শরী%'
   OR permanent_district LIKE '%শরিয়তপুর%'
   OR permanent_district LIKE '%শিরয়তপুর%';

UPDATE education.adm_applicant SET permanent_district = 'Sherpur'
WHERE permanent_district LIKE '%শের%';

UPDATE education.adm_applicant SET permanent_district = 'Sirajganj'
WHERE permanent_district LIKE '%সির%';

UPDATE education.adm_applicant SET permanent_district = 'Sunamganj'
WHERE permanent_district LIKE '%সু%';

UPDATE education.adm_applicant SET permanent_district = 'Sylhet'
WHERE permanent_district LIKE '%সিলে%';

UPDATE education.adm_applicant SET permanent_district = 'Tangail'
WHERE permanent_district LIKE '%টা%';

UPDATE education.adm_applicant SET permanent_district = 'Thakurgaon'
WHERE permanent_district LIKE '%ঠাকুরগাঁও%';


-- Present Upazila

UPDATE education.adm_applicant SET present_upazila = 'Aditmari'
WHERE present_upazila LIKE '%আদিত মারী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Adabor'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Adamdighi'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Abhaynagar'
WHERE present_upazila LIKE '%অভয়%';

UPDATE education.adm_applicant SET present_upazila = 'Agailjhara'
WHERE present_upazila LIKE '%আগইল%';

UPDATE education.adm_applicant SET present_upazila = 'Airport'
WHERE present_upazila LIKE '%এয়ারপোর্ট%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ajmiriganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Akbarshah'
WHERE present_upazila LIKE '%আকবরশাহ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Akhaura'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Akkelpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Alamdanga'
WHERE present_upazila LIKE '%আলমডাঙা%';

UPDATE education.adm_applicant SET present_upazila = 'Alfadanga'
WHERE present_upazila LIKE '%আলফাডা%';

UPDATE education.adm_applicant SET present_upazila = 'Ali Kadam'
WHERE present_upazila LIKE '%আলিকদম%'
   OR present_upazila LIKE '%আলীকদম%';

-- UPDATE education.adm_applicant SET present_upazila = 'Amtali'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Anwara'
WHERE present_upazila LIKE '%আন%'
   OR present_upazila LIKE '%আনোয়ারা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Araihazar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Aranghata'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Ashuganj'
WHERE present_upazila LIKE '%আশুগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Assasuni'
WHERE present_upazila LIKE '%আশাশুনি%';

UPDATE education.adm_applicant SET present_upazila = 'Atgharia'
WHERE present_upazila LIKE '%আটঘরিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Atpara'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Atrai'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Atwari'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Austagram'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Babuganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Badalgachhi'
WHERE present_upazila LIKE '%বদলগাছী%'
   OR present_upazila LIKE '%বদলগাজী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Badarganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Badda'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bagaichhari'
WHERE present_upazila LIKE '%বাঘাইছড়ি%'
   OR present_upazila LIKE '%বাঘাইছড়ি%'
   OR present_upazila LIKE '%সাজেক%';

UPDATE education.adm_applicant SET present_upazila = 'Bagatipara'
WHERE present_upazila LIKE '%বাগাতিপাড়া%'
   OR present_upazila LIKE '%বাগাতি%';

UPDATE education.adm_applicant SET present_upazila = 'Bagerhat Sadar'
WHERE present_upazila LIKE '%বাগেরহাট%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bagha'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bagherpara'
WHERE present_upazila LIKE '%বাঘার%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bagmara'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bahubal'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bajitpur'
WHERE present_upazila LIKE '%বাজিতপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Bakerganj'
WHERE present_upazila LIKE '%বাকেরগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Bakoliya'
WHERE present_upazila LIKE '%বাকলিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Baksiganj'
WHERE present_upazila LIKE '%বকশীগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Balaganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Baliadangi'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Baliakandi'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bamna'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Banani'
WHERE present_upazila LIKE '%বনানী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Banaripara'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bancharampur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chirirbandar'
WHERE present_upazila LIKE '%চিরির%';

UPDATE education.adm_applicant SET present_upazila = 'Bandar'
WHERE present_upazila LIKE '%বন্দর%';

UPDATE education.adm_applicant SET present_upazila = 'Bandarban Sadar'
WHERE present_upazila LIKE '%বান্দরবান%'
   OR present_upazila LIKE '%বান্দারবান%'
   OR present_upazila LIKE '%বান্দবান%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bangsal'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Baniyachong'
WHERE present_upazila LIKE '%বানি%';

UPDATE education.adm_applicant SET present_upazila = 'Banshkhali'
WHERE present_upazila LIKE '%বাঁশখালী%'
   OR present_upazila LIKE '%বাঁশ খালী%'
   OR present_upazila LIKE '%বাশঁখালী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Baraigram'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Barguna Sadar'
WHERE present_upazila LIKE '%বরগুনা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Barhatta'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Barishal Sadar'
WHERE present_upazila LIKE '%বরিশাল%';

UPDATE education.adm_applicant SET present_upazila = 'Barkal'
WHERE present_upazila LIKE '%বরক%';

-- UPDATE education.adm_applicant SET present_upazila = 'Barlekha'
-- WHERE present_upazila LIKE '%বরক%';

UPDATE education.adm_applicant SET present_upazila = 'Barura'
WHERE present_upazila LIKE '%বরু%';

-- UPDATE education.adm_applicant SET present_upazila = 'Basail'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bason'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bauphal'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bayazid'
WHERE present_upazila LIKE '%বায়%';

-- UPDATE education.adm_applicant SET present_upazila = 'Beanibazar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Begumganj'
WHERE present_upazila LIKE '%বেগ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Belabo'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Belaichhari'
WHERE present_upazila LIKE '%বিলাইছড়ি%';

UPDATE education.adm_applicant SET present_upazila = 'Belkuchi'
WHERE present_upazila LIKE '%বেলকুচি%';

UPDATE education.adm_applicant SET present_upazila = 'Bera'
WHERE present_upazila LIKE '%বেড়া%';

UPDATE education.adm_applicant SET present_upazila = 'Betagi'
WHERE present_upazila LIKE '%বেতাগী%';

UPDATE education.adm_applicant SET present_upazila = 'Bhaluka'
WHERE present_upazila LIKE '%ভালুকা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bhandaria'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bhanga'
WHERE present_upazila LIKE '%ভাংগা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bhangura'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bhashantek'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bhedarganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bheramara'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bhola Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bholahat'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bhuapur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bhurungamari'
WHERE present_upazila LIKE '%ভুরুঙ্গামারী%'
   OR present_upazila LIKE '%ভূরুঙ্গামারী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bijoynagar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bimanbandar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Birampur'
WHERE present_upazila LIKE '%বিরামপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Birganj'
WHERE present_upazila LIKE '%বীরগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Birol'
WHERE present_upazila LIKE '%বিরল%';

-- UPDATE education.adm_applicant SET present_upazila = 'Bishwamvarpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bishwanath'
WHERE present_upazila LIKE '%বিশ্বনাথ%';

UPDATE education.adm_applicant SET present_upazila = 'Bishwanath'
WHERE present_upazila LIKE '%বিশ্বনাথ%';

UPDATE education.adm_applicant SET present_upazila = 'Boalia'
WHERE present_upazila LIKE '%বোয়ালিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Boalkhali'
WHERE present_upazila LIKE '%বোয়ালখালী%';

UPDATE education.adm_applicant SET present_upazila = 'Boalkhali'
WHERE present_upazila LIKE '%বোয়ালখালী%';

UPDATE education.adm_applicant SET present_upazila = 'Bochaganj'
WHERE present_upazila LIKE '%বোচাগঞ্জ%'
   OR present_upazila LIKE '%বােঁচাগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Boda'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Bogura Sadar'
WHERE present_upazila LIKE '%বগুড়া%';

UPDATE education.adm_applicant SET present_upazila = 'Botiaghata'
WHERE present_upazila LIKE '%বটিয়াঘাটা%';

UPDATE education.adm_applicant SET present_upazila = 'Brahmanbaria Sadar'
WHERE present_upazila LIKE '%ব্রাক্ষণবাড়িয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Brahmanpara'
WHERE present_upazila LIKE '%ব্রাক্ষনপাড়া%'
   OR present_upazila LIKE '%ব্রাহ্মনপাড়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Burhanuddin'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Burichang'
WHERE present_upazila LIKE '%বুড়িচং%';

-- UPDATE education.adm_applicant SET present_upazila = 'Cantonment'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chakaria'
WHERE present_upazila LIKE '%চকর%';

UPDATE education.adm_applicant SET present_upazila = 'Chandanaish'
WHERE present_upazila LIKE '%চন্দ%';

UPDATE education.adm_applicant SET present_upazila = 'Chandgaon'
WHERE present_upazila LIKE '%চান্দগ%'
   OR present_upazila LIKE '%চাঁন্দ%';

UPDATE education.adm_applicant SET present_upazila = 'Chandina'
WHERE present_upazila LIKE '%চান্দিনা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chandpur Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chapainawabganj Sadar'
WHERE present_upazila LIKE '%চাঁপা%'
   OR present_upazila LIKE '%চাপাইনবাব%';

UPDATE education.adm_applicant SET present_upazila = 'Char Fasson'
WHERE present_upazila LIKE '%চরফ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Char Rajibpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Charbhadrasan'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Charghat'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chatkhil'
WHERE present_upazila LIKE '%চাটখিল%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chatmohar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chauddagram'
WHERE present_upazila LIKE '%চৌদ্দগ্রাম%'
   OR present_upazila LIKE '%চৌদ্দগ্রাম%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chaugachha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chauhali'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chawkbazar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chhagalnaiya'
WHERE present_upazila LIKE '%ছাগল%';

UPDATE education.adm_applicant SET present_upazila = 'Chhatak'
WHERE present_upazila LIKE '%ছাতক%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chilmari'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chirirbandar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Chitalmari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Chuadanga Sadar'
WHERE present_upazila LIKE '%চুয়াড%';

UPDATE education.adm_applicant SET present_upazila = 'Chunarughat'
WHERE present_upazila LIKE '%চুনারুঘাট%';

UPDATE education.adm_applicant SET present_upazila = 'Companiganj'
WHERE present_upazila LIKE '%কোম্পানীগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Cox''s Bazar Sadar'
WHERE present_upazila LIKE '%কক্সবাজার%';

UPDATE education.adm_applicant SET present_upazila = 'Cumilla Sadar Dakshin'
WHERE present_upazila LIKE '%কুমিল্লা সদর দ%';

UPDATE education.adm_applicant SET present_upazila = 'Cumilla Adarsha Sadar'
WHERE present_upazila LIKE '%আদর্শ সদর%'
   OR present_upazila LIKE '%কুমিল্লা সদর%';

UPDATE education.adm_applicant SET present_upazila = 'Daganbhuiyan'
WHERE present_upazila LIKE '%দাগন%';

UPDATE education.adm_applicant SET present_upazila = 'Dakkhinkhan'
WHERE present_upazila LIKE '%দক্ষিণ খ%';

UPDATE education.adm_applicant SET present_upazila = 'Dakop'
WHERE present_upazila LIKE '%দাকোপ%';

UPDATE education.adm_applicant SET present_upazila = 'Dakshin Surma'
WHERE present_upazila LIKE '%দক্ষিণ সুর%'
   OR present_upazila LIKE '%দক্ষিন সুরমা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Damudya'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Damurhuda'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Darussalam'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dasar'
-- WHERE present_upazila LIKE '%ডাসার%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dashmina'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Daudkandi'
WHERE present_upazila LIKE '%দাউ%';

UPDATE education.adm_applicant SET present_upazila = 'Daulatkhan'
WHERE present_upazila LIKE '%দৌলতখান%';

-- UPDATE education.adm_applicant SET present_upazila = 'Daulatpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Debhata'
WHERE present_upazila LIKE '%দেবহ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Debidwar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Debiganj'
WHERE present_upazila LIKE '%দেবীগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Delduar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Derai'
WHERE present_upazila LIKE '%ধিরাই%'
   OR present_upazila LIKE '%দিরাই%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dewanganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dhamoirhat'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Dhamrai'
WHERE present_upazila LIKE '%ধামরাই%';

UPDATE education.adm_applicant SET present_upazila = 'Dhanbari'
WHERE present_upazila LIKE '%ধন%'
   OR present_upazila LIKE '%ধানব%';

UPDATE education.adm_applicant SET present_upazila = 'Dhanmondi'
WHERE present_upazila LIKE '%ধানমন্ডি%';

UPDATE education.adm_applicant SET present_upazila = 'Dharamapasha'
WHERE present_upazila LIKE '%ধর্মপাসা%';

UPDATE education.adm_applicant SET present_upazila = 'Dhobaura'
WHERE present_upazila LIKE '%ধোবাউড়া%';

UPDATE education.adm_applicant SET present_upazila = 'Dhunat'
WHERE present_upazila LIKE '%ধুনট%';

UPDATE education.adm_applicant SET present_upazila = 'Dhupchanchia'
WHERE present_upazila LIKE '%দুপচাচিয়া%';


-- UPDATE education.adm_applicant SET present_upazila = 'Dighalia'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Dighinala'
WHERE present_upazila LIKE '%দিঘ%'
   OR present_upazila LIKE '%দীঘ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dimla'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Dinajpur Sadar'
WHERE present_upazila LIKE '%দিনাজপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dohar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Domar'
WHERE present_upazila LIKE '%ডোমার%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dowarabazar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Dumki'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Dumuria'
WHERE present_upazila LIKE '%ডুমুর%';

UPDATE education.adm_applicant SET present_upazila = 'Durgapur'
WHERE present_upazila LIKE '%দূর্গা%';

UPDATE education.adm_applicant SET present_upazila = 'Eidgaon'
WHERE present_upazila LIKE '%ঈ%';

UPDATE education.adm_applicant SET present_upazila = 'EPZ'
WHERE present_upazila LIKE '%ই,পি, জেড%'
   OR present_upazila LIKE '%ই.উ.জেড%';

-- UPDATE education.adm_applicant SET present_upazila = 'Fakirhat'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Faridganj'
WHERE present_upazila LIKE '%ফরিদগ%';

UPDATE education.adm_applicant SET present_upazila = 'Faridpur Sadar'
WHERE present_upazila LIKE '%ফরিদপুর স%';

UPDATE education.adm_applicant SET present_upazila = 'Faridpur'
WHERE present_upazila LIKE '%ফরিদপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Fatikchhari'
WHERE present_upazila LIKE '%ফটিকছড়ি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Fenchuganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Feni Sadar'
WHERE present_upazila LIKE '%ফেনী%'
   OR present_upazila LIKE '%চেমী%'
   OR present_upazila LIKE '%ফেণী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Fulbaria'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Fulgazi'
WHERE present_upazila LIKE '%ফুলগাজী%';

UPDATE education.adm_applicant SET present_upazila = 'Fultola'
WHERE present_upazila LIKE '%ফুলতলা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gacha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gafargaon'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Gaibandha Sadar'
WHERE present_upazila LIKE '%গাইবান্ধা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Galachipa'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gandaria'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Gangachhara'
WHERE present_upazila LIKE '%গংগাচড়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gangni'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gauripur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Gaurnadi'
WHERE present_upazila LIKE '%গৌরনদী%';

UPDATE education.adm_applicant SET present_upazila = 'Gazaria'
WHERE present_upazila LIKE '%গজারিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Gazipur Sadar'
WHERE present_upazila LIKE '%গাজীপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Ghatail'
WHERE present_upazila LIKE '%ঘটাইল%';

UPDATE education.adm_applicant SET present_upazila = 'Ghior'
WHERE present_upazila LIKE '%ঘিওর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ghoraghat'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Goalanda'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gobindaganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Godagari'
WHERE present_upazila LIKE '%গোদাগাড়ী%'
   OR present_upazila LIKE '%গোদাগাড়ী%';

UPDATE education.adm_applicant SET present_upazila = 'Golapganj'
WHERE present_upazila LIKE '%গোলাপগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Gomastapur'
WHERE present_upazila LIKE '%গোমস্তাপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Gopalganj Sadar'
WHERE present_upazila LIKE '%গোপালগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gopalpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gosairhat'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gowainghat'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Guimara'
WHERE present_upazila LIKE '%গুই%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gulshan'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Gurudaspur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Habiganj Sadar'
WHERE present_upazila LIKE '%হবিগঞ্জ%'
   OR present_upazila LIKE '%হাজিগঞ্জ%'
   OR present_upazila LIKE '%হবিগনজ%';

UPDATE education.adm_applicant SET present_upazila = 'Haimchar'
WHERE present_upazila LIKE '%হাইমচর%'
   OR present_upazila LIKE '%হাইম চর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Hakimpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Halishahar'
WHERE present_upazila LIKE '%হালিশহর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Haluaghat'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Harinakundu'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Harintana'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Haripur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Harirampur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Hathazari'
WHERE present_upazila LIKE '%হাটহাজারী%';

UPDATE education.adm_applicant SET present_upazila = 'Hatia'
WHERE present_upazila LIKE '%হাতিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Hatibandha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Hatirjheel'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Hazaribagh'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Haziganj'
WHERE present_upazila LIKE '%হাজীগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Hizla'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Homna'
WHERE present_upazila LIKE '%হোমনা%'
   OR present_upazila LIKE '%হোমনা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Hossainpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Indurkani'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ishwardi'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ishwarganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Islampur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Itna'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Jagannathpur'
WHERE present_upazila LIKE '%জগন্নাথপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Jaintiapur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Jaldhaka'
WHERE present_upazila LIKE '%জলঢাকা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Jamalganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Jamalpur Sadar'
WHERE present_upazila LIKE '%জামালপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Jashore Sadar'
WHERE present_upazila LIKE '%যশোর%'
   OR present_upazila LIKE '%যশোর%';

UPDATE education.adm_applicant SET present_upazila = 'Jatrabari'
WHERE present_upazila LIKE '%যাত্রাবাড়ি%';

UPDATE education.adm_applicant SET present_upazila = 'Jhalokati Sadar'
WHERE present_upazila LIKE '%ঝালকাঠি%';

UPDATE education.adm_applicant SET present_upazila = 'Jhenaidah Sadar'
WHERE present_upazila LIKE '%ঝিনাইদহ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Jhenaigati'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Jhikargachha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Jibannagar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Joypurhat Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Juraichhari'
WHERE present_upazila LIKE '%জুরাছ%'
   OR present_upazila LIKE '%জরাছড়ি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Juri'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kabirhat'
WHERE present_upazila LIKE '%কবিরহাট%';

UPDATE education.adm_applicant SET present_upazila = 'Kachua'
WHERE present_upazila LIKE '%কচুয়া%'
   OR present_upazila LIKE '%কুচুয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kadamrasul'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kadamtali'
WHERE present_upazila LIKE '%কদমতলী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kafrul'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kahaloo'
WHERE present_upazila LIKE '%কাহালু%';

UPDATE education.adm_applicant SET present_upazila = 'Kaharole'
WHERE present_upazila LIKE '%কাহারোল%';

UPDATE education.adm_applicant SET present_upazila = 'Kalabagan'
WHERE present_upazila LIKE '%কলাবাগান%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kalai'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kalapara'
WHERE present_upazila LIKE '%কলাপাড়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kalaroa'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kalia'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kaliakair'
WHERE present_upazila LIKE '%কালিয়%কৈর%';

UPDATE education.adm_applicant SET present_upazila = 'Kaliganj'
WHERE present_upazila LIKE '%কালীগঞ্জ%'
   OR present_upazila LIKE '%কালীগজ%';

UPDATE education.adm_applicant SET present_upazila = 'Kalihati'
WHERE present_upazila LIKE '%কালিহা%ত%';

UPDATE education.adm_applicant SET present_upazila = 'Kalkini'
WHERE present_upazila LIKE '%কালকিনি%'
   OR present_upazila LIKE '%কালকিনি%'
   OR present_upazila LIKE '%কারকিনী%'
   OR present_upazila LIKE '%কালকিনী%';

UPDATE education.adm_applicant SET present_upazila = 'Kalmakanda'
WHERE present_upazila LIKE '%কমলাকান্দা%';

UPDATE education.adm_applicant SET present_upazila = 'Kalukhali'
WHERE present_upazila LIKE '%কালুখালী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kamalganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kamalnagar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kamarkhanda'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kamrangirchar'
WHERE present_upazila LIKE '%কামরাঙ্গির%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kanaighat'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kapasia'
WHERE present_upazila LIKE '%কাপাসিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Kaptai'
WHERE present_upazila LIKE '%কাপ্তাই%';

-- UPDATE education.adm_applicant SET present_upazila = 'Karimganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Karnaphuli'
WHERE present_upazila LIKE '%কর্ণফুলী%'
   OR present_upazila LIKE '%কর্ণফুলি%'
   OR present_upazila LIKE '%কর্নফুলী%'
   OR present_upazila LIKE '%কর্ণফূলী%';

UPDATE education.adm_applicant SET present_upazila = 'Kasba'
WHERE present_upazila LIKE '%কসবা%';

UPDATE education.adm_applicant SET present_upazila = 'Kashiani'
WHERE present_upazila LIKE '%কাশিয়ানী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kashimpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kathalia'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Katiadi'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kaultia'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kaunia'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kawkhali'
WHERE present_upazila LIKE '%কাউ%খালী%'
   OR present_upazila LIKE '%কাউখালি%'
   OR present_upazila LIKE '%খাউখালী%';

UPDATE education.adm_applicant SET present_upazila = 'Kawnia'
WHERE present_upazila LIKE '%কাউনিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kazipur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kendua'
WHERE present_upazila LIKE '%কেন্দুয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Keraniganj'
WHERE present_upazila LIKE '%কেরা%গঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Keshabpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Khagrachhari'
WHERE present_upazila LIKE '%খাগড়াছড়ি%'
   OR present_upazila LIKE '%খাগড়াছাড়ি%'
   OR present_upazila LIKE '%খাগড়াছড়ি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Khaliajuri'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Khalishpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Khanjahan Ali'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Khansama'
WHERE present_upazila LIKE '%খানসামা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Khetlal'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Khilgaon'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Khilkhet'
WHERE present_upazila LIKE '%খিলক্ষেত%';

-- UPDATE education.adm_applicant SET present_upazila = 'Khoksa'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Khulna Sadar'
WHERE present_upazila LIKE '%খুলনা%';

UPDATE education.adm_applicant SET present_upazila = 'Khulshi'
WHERE present_upazila LIKE '%খুলশী%';

UPDATE education.adm_applicant SET present_upazila = 'Kishoreganj'
WHERE present_upazila LIKE '%কিশোরগঞ্জ%'
   OR present_upazila LIKE '%কিশােরগঞ্জ%'
   OR present_upazila LIKE '%কিশোরগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kishoreganj Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Konabari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kotalipara'
WHERE present_upazila LIKE '%কোটালীপাড়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kotchandpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kotwali'
WHERE present_upazila LIKE '%কোতয়ালী%'
   OR present_upazila LIKE '%কোতয়ালী%'
   OR present_upazila LIKE '%কতোয়ালী%';

UPDATE education.adm_applicant SET present_upazila = 'Koyra'
WHERE present_upazila LIKE '%কয়রা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kulaura'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Kuliarchar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Kumarkhali'
WHERE present_upazila LIKE '%কুমার%খালী%';

UPDATE education.adm_applicant SET present_upazila = 'Kurigram Sadar'
WHERE present_upazila LIKE '%কুড়িগ্রাম%';

UPDATE education.adm_applicant SET present_upazila = 'Kushtia Sadar'
WHERE present_upazila LIKE '%কুষ্টিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Kutubdia'
WHERE present_upazila LIKE '%কুতুবদিয়া%'
   OR present_upazila LIKE '%কুুতুবদিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Labanchora'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Lakhai'
WHERE present_upazila LIKE '%লাখাই%';

UPDATE education.adm_applicant SET present_upazila = 'Laksam'
WHERE present_upazila LIKE '%লাকসাম%';

UPDATE education.adm_applicant SET present_upazila = 'Lakshmipur Sadar'
WHERE present_upazila LIKE '%লক্ষ্মীপুর%'
   OR present_upazila LIKE '%লক্ষীপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Lalbagh'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Lalmai'
WHERE present_upazila LIKE '%লালমাই%';

-- UPDATE education.adm_applicant SET present_upazila = 'Lalmohan'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Lalmonirhat Sadar'
WHERE present_upazila LIKE '%লালমনিরহাট%';

-- UPDATE education.adm_applicant SET present_upazila = 'Lalpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Lama'
WHERE present_upazila LIKE '%লামা%'
   OR present_upazila LIKE '%লমা%';

UPDATE education.adm_applicant SET present_upazila = 'Langadu'
WHERE present_upazila LIKE '%লংগদু%';

UPDATE education.adm_applicant SET present_upazila = 'Laxmichhari'
WHERE present_upazila LIKE '%লক্ষীছড়ি%'
   OR present_upazila LIKE '%লক্ষ্মীছড়ি%';

UPDATE education.adm_applicant SET present_upazila = 'Lohagara'
WHERE present_upazila LIKE '%লোহাগ%'
   OR present_upazila LIKE '%লোহাগড়া%'
   OR present_upazila LIKE '%লোহাগাড়া%';

UPDATE education.adm_applicant SET present_upazila = 'Lohajang'
WHERE present_upazila LIKE '%লৌহজং%';

-- UPDATE education.adm_applicant SET present_upazila = 'Madan'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Madarganj'
WHERE present_upazila LIKE '%মাদারগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Madaripur Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Madhabpur'
WHERE present_upazila LIKE '%মাধবপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Madhukhali'
WHERE present_upazila LIKE '%মধুখালী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Madhupur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Madhyanagar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Magura Sadar'
WHERE present_upazila LIKE '%মাগুরা%';

UPDATE education.adm_applicant SET present_upazila = 'Mahalchhari'
WHERE present_upazila LIKE '%মহাল%';

-- UPDATE education.adm_applicant SET present_upazila = 'Maheshpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Manda'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Manikchhari'
WHERE present_upazila LIKE '%মানিকছড়ি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Manikgonj Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Manirampur'
WHERE present_upazila LIKE '%মণিরামপুর%'
   OR present_upazila LIKE '%মনিরামপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Manpura'
WHERE present_upazila LIKE '%মনপুরা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mathbaria'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Matiranga'
WHERE present_upazila LIKE '%মাটি%গা%'
   OR present_upazila LIKE '%মাটিরাঙা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Matlab Dakshin'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Matlab Uttar'
WHERE present_upazila LIKE '%মতলব উত্তর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Meghna'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Mehendiganj'
WHERE present_upazila LIKE '%মেহেন্দীগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Meherpur Sadar'
WHERE present_upazila LIKE '%মেহেরপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Melandaha'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Mirpur'
WHERE present_upazila LIKE '%মিরপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Mirsharai'
WHERE present_upazila LIKE '%মির%সরাই%'
   OR present_upazila LIKE '%মীরসরাই%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mirzaganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mirzapur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mithamain'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Mithapukur'
WHERE present_upazila LIKE '%মিঠাপুকুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mohadevpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Mohammadpur'
WHERE present_upazila LIKE '%মোহাম্মদপুর%'
   OR present_upazila LIKE '%মোহাম্মদ পুর%'
   OR present_upazila LIKE '%মুহম্মদপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mohanganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Mohanpur'
WHERE present_upazila LIKE '%মোহনপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Moheshkhali'
WHERE present_upazila LIKE '%মহ%খালী%'
   OR present_upazila LIKE '%মহেশখাঁলী%';

UPDATE education.adm_applicant SET present_upazila = 'Mollahat'
WHERE present_upazila LIKE '%মোল্লাহাট%';

UPDATE education.adm_applicant SET present_upazila = 'Mongla'
WHERE present_upazila LIKE '%মোংলা%';

UPDATE education.adm_applicant SET present_upazila = 'Monohardi'
WHERE present_upazila LIKE '%মনোহরদী%'
   OR present_upazila LIKE '%মনোহরদী%';

UPDATE education.adm_applicant SET present_upazila = 'Monohargonj'
WHERE present_upazila LIKE '%মনোহরগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Morrelganj'
WHERE present_upazila LIKE '%মোড়েলগঞ্জ%'
   OR present_upazila LIKE '%মোড়লগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Motihar'
WHERE present_upazila LIKE '%মতিহার%';

UPDATE education.adm_applicant SET present_upazila = 'Motijheel'
WHERE present_upazila LIKE '%মতিঝিল%';

-- UPDATE education.adm_applicant SET present_upazila = 'Moulvibazar Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mugda'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mujibnagar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Muksudpur'
WHERE present_upazila LIKE '%মুকসুদপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Muktagachha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Muladi'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Munshiganj Sadar'
WHERE present_upazila LIKE '%মুন্স%';

UPDATE education.adm_applicant SET present_upazila = 'Muradnagar'
WHERE present_upazila LIKE '%মুরাদনগর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Mymensingh Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Nabiganj'
WHERE present_upazila LIKE '%নবীগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Nabinagar'
WHERE present_upazila LIKE '%নবীনগর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nachol'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nagarkanda'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nagarpur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nageshwari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Naikhongchhari'
WHERE present_upazila LIKE '%নাইক্ষ্যংছড়ি%'
   OR present_upazila LIKE '%নাইক্ষ্য%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nakla'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Nalchity'
WHERE present_upazila LIKE '%নলছিটি%';

UPDATE education.adm_applicant SET present_upazila = 'Naldanga'
WHERE present_upazila LIKE '%নলডাঙ্গা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nalitabari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Nandail'
WHERE present_upazila LIKE '%নান্দাইল%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nandigram'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nangalkot'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Naniarchar'
WHERE present_upazila LIKE '%নানি%চর%'
   OR present_upazila LIKE '%নানুয়ারচর%'
   OR present_upazila LIKE '%নানীয়ার চর%';

UPDATE education.adm_applicant SET present_upazila = 'Naogaon Sadar'
WHERE present_upazila LIKE '%নওগা%';

UPDATE education.adm_applicant SET present_upazila = 'Narail Sadar'
WHERE present_upazila LIKE '%নড়াইল%';

UPDATE education.adm_applicant SET present_upazila = 'Narayanganj Sadar'
WHERE present_upazila LIKE '%নারায়নগঞ্জ%'
   OR present_upazila LIKE '%নারায়গঞ্জ%'
   OR present_upazila LIKE '%নারায়ণগ্ঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Naria'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Narsingdi Sadar'
WHERE present_upazila LIKE '%নরসিংদী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nasirnagar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Natore Sadar'
WHERE present_upazila LIKE '%নাটোর%'
   OR present_upazila LIKE '%নাটোর%';

UPDATE education.adm_applicant SET present_upazila = 'Nawabganj'
WHERE present_upazila LIKE 'নবাবগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Nazirpur'
WHERE present_upazila LIKE '%নজিরপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Nesarabad (Swarupkati)'
WHERE present_upazila LIKE '%নেছারাবাদ%';

UPDATE education.adm_applicant SET present_upazila = 'Netrokona Sadar'
WHERE present_upazila LIKE '%নেত্রকোনা%';

-- UPDATE education.adm_applicant SET present_upazila = 'New Market'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Niamatpur'
WHERE present_upazila LIKE '%নিয়ামতপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Nikli'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Nilphamari Sadar'
WHERE present_upazila LIKE '%নীলফামারী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Noakhali Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Osmani Nagar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Paba'
WHERE present_upazila LIKE '%পাবা%'
   OR present_upazila LIKE '%পবা%';

UPDATE education.adm_applicant SET present_upazila = 'Pabna Sadar'
WHERE present_upazila LIKE '%পাবনা%';

UPDATE education.adm_applicant SET present_upazila = 'Pahartali'
WHERE present_upazila LIKE '%পাহাড়তলী%';

UPDATE education.adm_applicant SET present_upazila = 'Paikgachha'
WHERE present_upazila LIKE '%পাইকগাছা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Pakundia'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Palash'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Palashbari'
WHERE present_upazila LIKE '%পলাশবাড়ী%';

UPDATE education.adm_applicant SET present_upazila = 'Pallabi'
WHERE present_upazila LIKE '%পল্লবী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Paltan'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Panchagarh Sadar'
WHERE present_upazila LIKE '%পঞ্চগড়%';

UPDATE education.adm_applicant SET present_upazila = 'Panchbibi'
WHERE present_upazila LIKE '%পাঁচবিবি%'
   OR present_upazila LIKE '%পাঁবিবি%';

UPDATE education.adm_applicant SET present_upazila = 'Panchhari'
WHERE present_upazila LIKE '%পানছড়ি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Panchlaish'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Pangsha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Parbatipur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Parshuram'
WHERE present_upazila LIKE '%পরশুরাম%';

-- UPDATE education.adm_applicant SET present_upazila = 'Patenga'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Patgram'
WHERE present_upazila LIKE '%পাটগ্রাম%';

-- UPDATE education.adm_applicant SET present_upazila = 'Patharghata'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Patiya'
WHERE present_upazila LIKE '%পটিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Patnitala'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Patuakhali Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Pekua'
WHERE present_upazila LIKE '%পেকুয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Phulbari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Phulchhari'
WHERE present_upazila LIKE '%ফুলছড়ি%';

UPDATE education.adm_applicant SET present_upazila = 'Phulpur'
WHERE present_upazila LIKE '%ফুলপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Pirgachha'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Pirganj'
WHERE present_upazila LIKE '%পীরগঞ্জ%'
   OR present_upazila LIKE '%পিরগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Pirojpur Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Porsha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Pubail'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Purbadhala'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Puthia'
WHERE present_upazila LIKE '%পুঠিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Raiganj'
WHERE present_upazila LIKE '%রায়গঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Raipur'
WHERE present_upazila LIKE '%রায়পুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Raipura'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Rajapur'
WHERE present_upazila LIKE '%রাজাপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Rajarhat'
WHERE present_upazila LIKE '%রাজারহাট%';

UPDATE education.adm_applicant SET present_upazila = 'Rajasthali'
WHERE present_upazila LIKE '%রাজস্থলী%'
   OR present_upazila LIKE '%রাজস্হলী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Rajbari Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Rajnagar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Rajoir'
WHERE present_upazila LIKE '%রাজৈর%';

UPDATE education.adm_applicant SET present_upazila = 'Rajpara'
WHERE present_upazila LIKE '%রাজপাড়া%';

UPDATE education.adm_applicant SET present_upazila = 'Ramganj'
WHERE present_upazila LIKE '%রামগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Ramgarh'
WHERE present_upazila LIKE '%রামগড়%'
   OR present_upazila LIKE '%রামগর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ramgati'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ramna'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Rampal'
WHERE present_upazila LIKE '%রামপাল%';

-- UPDATE education.adm_applicant SET present_upazila = 'Rampura'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Ramu'
WHERE present_upazila LIKE '%রামু%';

-- UPDATE education.adm_applicant SET present_upazila = 'Rangabali'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Rangamati Sadar'
WHERE present_upazila LIKE '%রাঙ্গামাটি%'
   OR present_upazila LIKE '%র%মাটি%';

UPDATE education.adm_applicant SET present_upazila = 'Rangpur Sadar'
WHERE present_upazila LIKE '%রংপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Rangunia'
WHERE present_upazila LIKE '%রাঙ্গুন%'
   OR present_upazila LIKE '%রাংগুনিয়া%'
   OR present_upazila LIKE '%রাংগুনীয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Raninagar'
WHERE present_upazila LIKE '%রাণীনগর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ranisankail'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Raozan'
WHERE present_upazila LIKE '%রাউজান%';

UPDATE education.adm_applicant SET present_upazila = 'Rowangchhari'
WHERE present_upazila LIKE '%রোয়াংছড়ি%'
   OR present_upazila LIKE '%রোয়াংছড়ি%'
   OR present_upazila LIKE '%রোয়াংছড়ি%'
   OR present_upazila LIKE '%রোয়াছড়ি%'
   OR present_upazila LIKE '%রোয়ারংছড়ি%'
   OR present_upazila LIKE '%রোয়াছড়ি%'
   OR present_upazila LIKE '%রোয়াংছাড়ি%'
   OR present_upazila LIKE '%রোয়াংচড়ি%'
   OR present_upazila LIKE '%রোয়াংছাড়ি%'
   OR present_upazila LIKE '%রােয়াংছড়ি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Rowmari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Ruma'
WHERE present_upazila LIKE '%রুমা%';

UPDATE education.adm_applicant SET present_upazila = 'Rupganj'
WHERE present_upazila LIKE '%রূপগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Rupnagar'
WHERE present_upazila LIKE '%রূপনগর%';

UPDATE education.adm_applicant SET present_upazila = 'Rupsha'
WHERE present_upazila LIKE '%রূপসা%'
   OR present_upazila LIKE '%রুপসা%'
   OR present_upazila LIKE '%রুপসা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sabujbagh'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sadarghat'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sadarpur'
WHERE present_upazila LIKE '%সদরপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Sadullapur'
WHERE present_upazila LIKE '%সাদুল্লা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sakhipur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Saltha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sandwip'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Santhia'
WHERE present_upazila LIKE '%সাঁথিয়া%'
   OR present_upazila LIKE '%সাথিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sapahar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sarail'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sarankhola'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sariakandi'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sarishabari'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Satkania'
WHERE present_upazila LIKE '%সাতকানিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Satkhira Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Saturia'
WHERE present_upazila LIKE '%সাটুরিয়া%';

UPDATE education.adm_applicant SET present_upazila = 'Savar'
WHERE present_upazila LIKE '%সাভার%';

UPDATE education.adm_applicant SET present_upazila = 'Senbagh'
WHERE present_upazila LIKE '%সেনবাগ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shah Ali'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Shah Makhdum'
WHERE present_upazila LIKE '%শাহমখদুম%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shahbag'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shahjadpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Shahjahanpur'
WHERE present_upazila LIKE '%সাজাহান পূর%';

UPDATE education.adm_applicant SET present_upazila = 'Shahrasti'
WHERE present_upazila LIKE '%শাহরাস্তি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shailkupa'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shajahanpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Shalikha'
WHERE present_upazila LIKE '%শালিখা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shalla'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shantiganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shariatpur Sadar'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sharsha'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shayestaganj'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sher-E-Bangla Nagar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sherpur'
WHERE present_upazila LIKE '%শেরপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sherpur Sadar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Shibchar'
WHERE present_upazila LIKE '%শিবচর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shibganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Shibpur'
WHERE present_upazila LIKE '%শিবপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shilpanchal'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shivalaya'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Shyamnagar'
WHERE present_upazila LIKE '%শ্যামনগ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Shyampur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Siddhirganj'
WHERE present_upazila LIKE '%সিদ্ধরগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Singair'
WHERE present_upazila LIKE '%সিংগাইর%';

UPDATE education.adm_applicant SET present_upazila = 'Singra'
WHERE present_upazila LIKE '%সিংড়া%';

UPDATE education.adm_applicant SET present_upazila = 'Sirajdikhan'
WHERE present_upazila LIKE '%সিরাজদিখান%';

UPDATE education.adm_applicant SET present_upazila = 'Sirajganj Sadar'
WHERE present_upazila LIKE '%সিরাজগঞ্জ%'
   OR present_upazila LIKE '%সিসাজগঞ্জ%';

UPDATE education.adm_applicant SET present_upazila = 'Sitakunda'
WHERE present_upazila LIKE '%সীতাকুন্ড%'
   OR present_upazila LIKE '%সিতাকুন্ড%'
   OR present_upazila LIKE '%শীতাকুন্ড%';

UPDATE education.adm_applicant SET present_upazila = 'Sonadangha'
WHERE present_upazila LIKE '%সোনাডাঙ্গা%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sonagazi'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sonaimuri'
WHERE present_upazila LIKE '%সোনাইমুড়ী%';

UPDATE education.adm_applicant SET present_upazila = 'Sonargaon'
WHERE present_upazila LIKE '%সোনারগাঁ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sonatola'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sreebardi'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sreemangal'
WHERE present_upazila LIKE '%শ্রীমঙ্গল%';

UPDATE education.adm_applicant SET present_upazila = 'Sreenagar'
WHERE present_upazila LIKE '%শ্রীনগর%';

UPDATE education.adm_applicant SET present_upazila = 'Sreepur'
WHERE present_upazila LIKE '%শ্রীপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Subarnachar'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sughatta'
WHERE present_upazila LIKE '%সাঘাটা%';

UPDATE education.adm_applicant SET present_upazila = 'Sujanagar'
WHERE present_upazila LIKE '%সুজানগর%';

UPDATE education.adm_applicant SET present_upazila = 'Sunamganj Sadar'
WHERE present_upazila LIKE '%সুনামগঞ্জ সদর%'
   OR present_upazila LIKE '%সুনামগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Sundarganj'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sutrapur'
WHERE present_upazila LIKE '%সূত্রাপুর%'
   OR present_upazila LIKE '%সুত্রাপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Syedpur'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Sylhet Sadar'
WHERE present_upazila LIKE '%সিলেট%';

UPDATE education.adm_applicant SET present_upazila = 'Tahirpur'
WHERE present_upazila LIKE '%তাহিরপুর%';

UPDATE education.adm_applicant SET present_upazila = 'Tala'
WHERE present_upazila LIKE '%তাল%';

UPDATE education.adm_applicant SET present_upazila = 'Taltali'
WHERE present_upazila LIKE '%তালতলী%';

UPDATE education.adm_applicant SET present_upazila = 'Tangail Sadar'
WHERE present_upazila LIKE '%টাঙ্গাইল%';

UPDATE education.adm_applicant SET present_upazila = 'Tanore'
WHERE present_upazila LIKE '%তানোর%';

UPDATE education.adm_applicant SET present_upazila = 'Tara Khanda'
WHERE present_upazila LIKE '%তারাকান্দা%';

UPDATE education.adm_applicant SET present_upazila = 'Taraganj'
WHERE present_upazila LIKE '%তারাগঞ্জ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Tarail'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Tarash'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Tazumuddin'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Tejgaon'
WHERE present_upazila LIKE '%তেজগাঁও%';

UPDATE education.adm_applicant SET present_upazila = 'Teknaf'
WHERE present_upazila LIKE '%টেকনাফ%';

-- UPDATE education.adm_applicant SET present_upazila = 'Terokhada'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Tetulia'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Thakurgaon Sadar'
WHERE present_upazila LIKE '%ঠাকুরগাঁও%';

UPDATE education.adm_applicant SET present_upazila = 'Thanchi'
WHERE present_upazila LIKE '%থানচি%'
   OR present_upazila LIKE '%থানছি%';

-- UPDATE education.adm_applicant SET present_upazila = 'Titas'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Tongi'
WHERE present_upazila LIKE '%টংগী%';

UPDATE education.adm_applicant SET present_upazila = 'Tongibari'
WHERE present_upazila LIKE '%টংগীবাড়ী%'
   OR present_upazila LIKE '%টংগিবাড়ী%';

-- UPDATE education.adm_applicant SET present_upazila = 'Trishal'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Tungipara'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Turag'
WHERE present_upazila LIKE '%তুরাগ%';

UPDATE education.adm_applicant SET present_upazila = 'Ukhiya'
WHERE present_upazila LIKE '%উখিয়া%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ulipur'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Ullahpara'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Uttara East'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Uttara West'
-- WHERE present_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET present_upazila = 'Uttarkhan'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Vatara'
WHERE present_upazila LIKE '%ভাটারা%';

UPDATE education.adm_applicant SET present_upazila = 'Wari'
WHERE present_upazila LIKE '%ওয়ারী%';

UPDATE education.adm_applicant SET present_upazila = 'Wazirpur'
WHERE present_upazila LIKE '%উজিরপুর%';

-- UPDATE education.adm_applicant SET present_upazila = 'Zajira'
-- WHERE present_upazila LIKE '%%';

UPDATE education.adm_applicant SET present_upazila = 'Zakiganj'
WHERE present_upazila LIKE '%জকিগঞ্জ%';

-- Permanent Upzila

UPDATE education.adm_applicant SET permanent_upazila = 'Aditmari'
WHERE permanent_upazila LIKE '%আদিত মারী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Adabor'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Adamdighi'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Abhaynagar'
WHERE permanent_upazila LIKE '%অভয়%';

UPDATE education.adm_applicant SET permanent_upazila = 'Agailjhara'
WHERE permanent_upazila LIKE '%আগইল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Airport'
WHERE permanent_upazila LIKE '%এয়ারপোর্ট%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ajmiriganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Akbarshah'
WHERE permanent_upazila LIKE '%আকবরশাহ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Akhaura'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Akkelpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Alamdanga'
WHERE permanent_upazila LIKE '%আলমডাঙা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Alfadanga'
WHERE permanent_upazila LIKE '%আলফাডা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ali Kadam'
WHERE permanent_upazila LIKE '%আলিকদম%'
   OR permanent_upazila LIKE '%আলীকদম%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Amtali'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Anwara'
WHERE permanent_upazila LIKE '%আন%'
   OR permanent_upazila LIKE '%আনোয়ারা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Araihazar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Aranghata'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ashuganj'
WHERE permanent_upazila LIKE '%আশুগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Assasuni'
WHERE permanent_upazila LIKE '%আশাশুনি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Atgharia'
WHERE permanent_upazila LIKE '%আটঘরিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Atpara'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Atrai'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Atwari'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Austagram'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Babuganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Badalgachhi'
WHERE permanent_upazila LIKE '%বদলগাছী%'
   OR permanent_upazila LIKE '%বদলগাজী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Badarganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Badda'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bagaichhari'
WHERE permanent_upazila LIKE '%বাঘাইছড়ি%'
   OR permanent_upazila LIKE '%বাঘাইছড়ি%'
   OR permanent_upazila LIKE '%সাজেক%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bagatipara'
WHERE permanent_upazila LIKE '%বাগাতিপাড়া%'
   OR permanent_upazila LIKE '%বাগাতি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bagerhat Sadar'
WHERE permanent_upazila LIKE '%বাগেরহাট%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bagha'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bagherpara'
WHERE permanent_upazila LIKE '%বাঘার%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bagmara'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bahubal'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bajitpur'
WHERE permanent_upazila LIKE '%বাজিতপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bakerganj'
WHERE permanent_upazila LIKE '%বাকেরগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bakoliya'
WHERE permanent_upazila LIKE '%বাকলিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Baksiganj'
WHERE permanent_upazila LIKE '%বকশীগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Balaganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Baliadangi'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Baliakandi'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bamna'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Banani'
WHERE permanent_upazila LIKE '%বনানী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Banaripara'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bancharampur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chirirbandar'
WHERE permanent_upazila LIKE '%চিরির%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bandar'
WHERE permanent_upazila LIKE '%বন্দর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bandarban Sadar'
WHERE permanent_upazila LIKE '%বান্দরবান%'
   OR permanent_upazila LIKE '%বান্দারবান%'
   OR permanent_upazila LIKE '%বান্দবান%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bangsal'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Baniyachong'
WHERE permanent_upazila LIKE '%বানি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Banshkhali'
WHERE permanent_upazila LIKE '%বাঁশখালী%'
   OR permanent_upazila LIKE '%বাঁশ খালী%'
   OR permanent_upazila LIKE '%বাশঁখালী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Baraigram'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Barguna Sadar'
WHERE permanent_upazila LIKE '%বরগুনা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Barhatta'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Barishal Sadar'
WHERE permanent_upazila LIKE '%বরিশাল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Barkal'
WHERE permanent_upazila LIKE '%বরক%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Barlekha'
-- WHERE permanent_upazila LIKE '%বরক%';

UPDATE education.adm_applicant SET permanent_upazila = 'Barura'
WHERE permanent_upazila LIKE '%বরু%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Basail'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bason'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bauphal'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bayazid'
WHERE permanent_upazila LIKE '%বায়%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Beanibazar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Begumganj'
WHERE permanent_upazila LIKE '%বেগ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Belabo'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Belaichhari'
WHERE permanent_upazila LIKE '%বিলাইছড়ি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Belkuchi'
WHERE permanent_upazila LIKE '%বেলকুচি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bera'
WHERE permanent_upazila LIKE '%বেড়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Betagi'
WHERE permanent_upazila LIKE '%বেতাগী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bhaluka'
WHERE permanent_upazila LIKE '%ভালুকা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bhandaria'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bhanga'
WHERE permanent_upazila LIKE '%ভাংগা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bhangura'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bhashantek'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bhedarganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bheramara'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bhola Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bholahat'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bhuapur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bhurungamari'
WHERE permanent_upazila LIKE '%ভুরুঙ্গামারী%'
   OR permanent_upazila LIKE '%ভূরুঙ্গামারী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bijoynagar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bimanbandar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Birampur'
WHERE permanent_upazila LIKE '%বিরামপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Birganj'
WHERE permanent_upazila LIKE '%বীরগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Birol'
WHERE permanent_upazila LIKE '%বিরল%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Bishwamvarpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bishwanath'
WHERE permanent_upazila LIKE '%বিশ্বনাথ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bishwanath'
WHERE permanent_upazila LIKE '%বিশ্বনাথ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Boalia'
WHERE permanent_upazila LIKE '%বোয়ালিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Boalkhali'
WHERE permanent_upazila LIKE '%বোয়ালখালী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Boalkhali'
WHERE permanent_upazila LIKE '%বোয়ালখালী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bochaganj'
WHERE permanent_upazila LIKE '%বোচাগঞ্জ%'
   OR permanent_upazila LIKE '%বােঁচাগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Boda'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Bogura Sadar'
WHERE permanent_upazila LIKE '%বগুড়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Botiaghata'
WHERE permanent_upazila LIKE '%বটিয়াঘাটা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Brahmanbaria Sadar'
WHERE permanent_upazila LIKE '%ব্রাক্ষণবাড়িয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Brahmanpara'
WHERE permanent_upazila LIKE '%ব্রাক্ষনপাড়া%'
   OR permanent_upazila LIKE '%ব্রাহ্মনপাড়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Burhanuddin'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Burichang'
WHERE permanent_upazila LIKE '%বুড়িচং%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Cantonment'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chakaria'
WHERE permanent_upazila LIKE '%চকর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chandanaish'
WHERE permanent_upazila LIKE '%চন্দ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chandgaon'
WHERE permanent_upazila LIKE '%চান্দগ%'
   OR permanent_upazila LIKE '%চাঁন্দ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chandina'
WHERE permanent_upazila LIKE '%চান্দিনা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chandpur Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chapainawabganj Sadar'
WHERE permanent_upazila LIKE '%চাঁপা%'
   OR permanent_upazila LIKE '%চাপাইনবাব%';

UPDATE education.adm_applicant SET permanent_upazila = 'Char Fasson'
WHERE permanent_upazila LIKE '%চরফ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Char Rajibpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Charbhadrasan'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Charghat'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chatkhil'
WHERE permanent_upazila LIKE '%চাটখিল%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chatmohar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chauddagram'
WHERE permanent_upazila LIKE '%চৌদ্দগ্রাম%'
   OR permanent_upazila LIKE '%চৌদ্দগ্রাম%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chaugachha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chauhali'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chawkbazar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chhagalnaiya'
WHERE permanent_upazila LIKE '%ছাগল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chhatak'
WHERE permanent_upazila LIKE '%ছাতক%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chilmari'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chirirbandar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Chitalmari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chuadanga Sadar'
WHERE permanent_upazila LIKE '%চুয়াড%';

UPDATE education.adm_applicant SET permanent_upazila = 'Chunarughat'
WHERE permanent_upazila LIKE '%চুনারুঘাট%';

UPDATE education.adm_applicant SET permanent_upazila = 'Companiganj'
WHERE permanent_upazila LIKE '%কোম্পানীগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Cox''s Bazar Sadar'
WHERE permanent_upazila LIKE '%কক্সবাজার%';

UPDATE education.adm_applicant SET permanent_upazila = 'Cumilla Sadar Dakshin'
WHERE permanent_upazila LIKE '%কুমিল্লা সদর দ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Cumilla Adarsha Sadar'
WHERE permanent_upazila LIKE '%আদর্শ সদর%'
   OR permanent_upazila LIKE '%কুমিল্লা সদর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Daganbhuiyan'
WHERE permanent_upazila LIKE '%দাগন%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dakkhinkhan'
WHERE permanent_upazila LIKE '%দক্ষিণ খ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dakop'
WHERE permanent_upazila LIKE '%দাকোপ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dakshin Surma'
WHERE permanent_upazila LIKE '%দক্ষিণ সুর%'
   OR permanent_upazila LIKE '%দক্ষিন সুরমা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Damudya'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Damurhuda'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Darussalam'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dasar'
-- WHERE permanent_upazila LIKE '%ডাসার%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dashmina'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Daudkandi'
WHERE permanent_upazila LIKE '%দাউ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Daulatkhan'
WHERE permanent_upazila LIKE '%দৌলতখান%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Daulatpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Debhata'
WHERE permanent_upazila LIKE '%দেবহ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Debidwar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Debiganj'
WHERE permanent_upazila LIKE '%দেবীগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Delduar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Derai'
WHERE permanent_upazila LIKE '%ধিরাই%'
   OR permanent_upazila LIKE '%দিরাই%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dewanganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dhamoirhat'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dhamrai'
WHERE permanent_upazila LIKE '%ধামরাই%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dhanbari'
WHERE permanent_upazila LIKE '%ধন%'
   OR permanent_upazila LIKE '%ধানব%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dhanmondi'
WHERE permanent_upazila LIKE '%ধানমন্ডি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dharamapasha'
WHERE permanent_upazila LIKE '%ধর্মপাসা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dhobaura'
WHERE permanent_upazila LIKE '%ধোবাউড়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dhunat'
WHERE permanent_upazila LIKE '%ধুনট%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dhupchanchia'
WHERE permanent_upazila LIKE '%দুপচাচিয়া%';


-- UPDATE education.adm_applicant SET permanent_upazila = 'Dighalia'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dighinala'
WHERE permanent_upazila LIKE '%দিঘ%'
   OR permanent_upazila LIKE '%দীঘ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dimla'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dinajpur Sadar'
WHERE permanent_upazila LIKE '%দিনাজপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dohar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Domar'
WHERE permanent_upazila LIKE '%ডোমার%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dowarabazar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Dumki'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Dumuria'
WHERE permanent_upazila LIKE '%ডুমুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Durgapur'
WHERE permanent_upazila LIKE '%দূর্গা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Eidgaon'
WHERE permanent_upazila LIKE '%ঈ%';

UPDATE education.adm_applicant SET permanent_upazila = 'EPZ'
WHERE permanent_upazila LIKE '%ই,পি, জেড%'
   OR permanent_upazila LIKE '%ই.উ.জেড%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Fakirhat'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Faridganj'
WHERE permanent_upazila LIKE '%ফরিদগ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Faridpur Sadar'
WHERE permanent_upazila LIKE '%ফরিদপুর স%';

UPDATE education.adm_applicant SET permanent_upazila = 'Faridpur'
WHERE permanent_upazila LIKE '%ফরিদপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Fatikchhari'
WHERE permanent_upazila LIKE '%ফটিকছড়ি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Fenchuganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Feni Sadar'
WHERE permanent_upazila LIKE '%ফেনী%'
   OR permanent_upazila LIKE '%চেমী%'
   OR permanent_upazila LIKE '%ফেণী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Fulbaria'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Fulgazi'
WHERE permanent_upazila LIKE '%ফুলগাজী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Fultola'
WHERE permanent_upazila LIKE '%ফুলতলা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gacha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gafargaon'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gaibandha Sadar'
WHERE permanent_upazila LIKE '%গাইবান্ধা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Galachipa'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gandaria'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gangachhara'
WHERE permanent_upazila LIKE '%গংগাচড়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gangni'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gauripur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gaurnadi'
WHERE permanent_upazila LIKE '%গৌরনদী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gazaria'
WHERE permanent_upazila LIKE '%গজারিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gazipur Sadar'
WHERE permanent_upazila LIKE '%গাজীপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ghatail'
WHERE permanent_upazila LIKE '%ঘটাইল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ghior'
WHERE permanent_upazila LIKE '%ঘিওর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ghoraghat'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Goalanda'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gobindaganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Godagari'
WHERE permanent_upazila LIKE '%গোদাগাড়ী%'
   OR permanent_upazila LIKE '%গোদাগাড়ী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Golapganj'
WHERE permanent_upazila LIKE '%গোলাপগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gomastapur'
WHERE permanent_upazila LIKE '%গোমস্তাপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Gopalganj Sadar'
WHERE permanent_upazila LIKE '%গোপালগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gopalpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gosairhat'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gowainghat'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Guimara'
WHERE permanent_upazila LIKE '%গুই%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gulshan'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Gurudaspur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Habiganj Sadar'
WHERE permanent_upazila LIKE '%হবিগঞ্জ%'
   OR permanent_upazila LIKE '%হাজিগঞ্জ%'
   OR permanent_upazila LIKE '%হবিগনজ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Haimchar'
WHERE permanent_upazila LIKE '%হাইমচর%'
   OR permanent_upazila LIKE '%হাইম চর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Hakimpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Halishahar'
WHERE permanent_upazila LIKE '%হালিশহর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Haluaghat'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Harinakundu'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Harintana'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Haripur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Harirampur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Hathazari'
WHERE permanent_upazila LIKE '%হাটহাজারী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Hatia'
WHERE permanent_upazila LIKE '%হাতিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Hatibandha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Hatirjheel'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Hazaribagh'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Haziganj'
WHERE permanent_upazila LIKE '%হাজীগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Hizla'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Homna'
WHERE permanent_upazila LIKE '%হোমনা%'
   OR permanent_upazila LIKE '%হোমনা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Hossainpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Indurkani'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ishwardi'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ishwarganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Islampur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Itna'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jagannathpur'
WHERE permanent_upazila LIKE '%জগন্নাথপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Jaintiapur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jaldhaka'
WHERE permanent_upazila LIKE '%জলঢাকা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Jamalganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jamalpur Sadar'
WHERE permanent_upazila LIKE '%জামালপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jashore Sadar'
WHERE permanent_upazila LIKE '%যশোর%'
   OR permanent_upazila LIKE '%যশোর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jatrabari'
WHERE permanent_upazila LIKE '%যাত্রাবাড়ি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jhalokati Sadar'
WHERE permanent_upazila LIKE '%ঝালকাঠি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Jhenaidah Sadar'
WHERE permanent_upazila LIKE '%ঝিনাইদহ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Jhenaigati'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Jhikargachha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Jibannagar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Joypurhat Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Juraichhari'
WHERE permanent_upazila LIKE '%জুরাছ%'
   OR permanent_upazila LIKE '%জরাছড়ি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Juri'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kabirhat'
WHERE permanent_upazila LIKE '%কবিরহাট%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kachua'
WHERE permanent_upazila LIKE '%কচুয়া%'
   OR permanent_upazila LIKE '%কুচুয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kadamrasul'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kadamtali'
WHERE permanent_upazila LIKE '%কদমতলী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kafrul'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kahaloo'
WHERE permanent_upazila LIKE '%কাহালু%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kaharole'
WHERE permanent_upazila LIKE '%কাহারোল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kalabagan'
WHERE permanent_upazila LIKE '%কলাবাগান%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kalai'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kalapara'
WHERE permanent_upazila LIKE '%কলাপাড়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kalaroa'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kalia'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kaliakair'
WHERE permanent_upazila LIKE '%কালিয়%কৈর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kaliganj'
WHERE permanent_upazila LIKE '%কালীগঞ্জ%'
   OR permanent_upazila LIKE '%কালীগজ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kalihati'
WHERE permanent_upazila LIKE '%কালিহা%ত%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kalkini'
WHERE permanent_upazila LIKE '%কালকিনি%'
   OR permanent_upazila LIKE '%কালকিনি%'
   OR permanent_upazila LIKE '%কারকিনী%'
   OR permanent_upazila LIKE '%কালকিনী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kalmakanda'
WHERE permanent_upazila LIKE '%কমলাকান্দা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kalukhali'
WHERE permanent_upazila LIKE '%কালুখালী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kamalganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kamalnagar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kamarkhanda'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kamrangirchar'
WHERE permanent_upazila LIKE '%কামরাঙ্গির%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kanaighat'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kapasia'
WHERE permanent_upazila LIKE '%কাপাসিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kaptai'
WHERE permanent_upazila LIKE '%কাপ্তাই%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Karimganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Karnaphuli'
WHERE permanent_upazila LIKE '%কর্ণফুলী%'
   OR permanent_upazila LIKE '%কর্ণফুলি%'
   OR permanent_upazila LIKE '%কর্নফুলী%'
   OR permanent_upazila LIKE '%কর্ণফূলী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kasba'
WHERE permanent_upazila LIKE '%কসবা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kashiani'
WHERE permanent_upazila LIKE '%কাশিয়ানী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kashimpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kathalia'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Katiadi'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kaultia'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kaunia'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kawkhali'
WHERE permanent_upazila LIKE '%কাউ%খালী%'
   OR permanent_upazila LIKE '%কাউখালি%'
   OR permanent_upazila LIKE '%খাউখালী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kawnia'
WHERE permanent_upazila LIKE '%কাউনিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kazipur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kendua'
WHERE permanent_upazila LIKE '%কেন্দুয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Keraniganj'
WHERE permanent_upazila LIKE '%কেরা%গঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Keshabpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Khagrachhari'
WHERE permanent_upazila LIKE '%খাগড়াছড়ি%'
   OR permanent_upazila LIKE '%খাগড়াছাড়ি%'
   OR permanent_upazila LIKE '%খাগড়াছড়ি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Khaliajuri'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Khalishpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Khanjahan Ali'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Khansama'
WHERE permanent_upazila LIKE '%খানসামা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Khetlal'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Khilgaon'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Khilkhet'
WHERE permanent_upazila LIKE '%খিলক্ষেত%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Khoksa'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Khulna Sadar'
WHERE permanent_upazila LIKE '%খুলনা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Khulshi'
WHERE permanent_upazila LIKE '%খুলশী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kishoreganj'
WHERE permanent_upazila LIKE '%কিশোরগঞ্জ%'
   OR permanent_upazila LIKE '%কিশােরগঞ্জ%'
   OR permanent_upazila LIKE '%কিশোরগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kishoreganj Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Konabari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kotalipara'
WHERE permanent_upazila LIKE '%কোটালীপাড়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kotchandpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kotwali'
WHERE permanent_upazila LIKE '%কোতয়ালী%'
   OR permanent_upazila LIKE '%কোতয়ালী%'
   OR permanent_upazila LIKE '%কতোয়ালী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Koyra'
WHERE permanent_upazila LIKE '%কয়রা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kulaura'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Kuliarchar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kumarkhali'
WHERE permanent_upazila LIKE '%কুমার%খালী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kurigram Sadar'
WHERE permanent_upazila LIKE '%কুড়িগ্রাম%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kushtia Sadar'
WHERE permanent_upazila LIKE '%কুষ্টিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Kutubdia'
WHERE permanent_upazila LIKE '%কুতুবদিয়া%'
   OR permanent_upazila LIKE '%কুুতুবদিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Labanchora'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lakhai'
WHERE permanent_upazila LIKE '%লাখাই%';

UPDATE education.adm_applicant SET permanent_upazila = 'Laksam'
WHERE permanent_upazila LIKE '%লাকসাম%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lakshmipur Sadar'
WHERE permanent_upazila LIKE '%লক্ষ্মীপুর%'
   OR permanent_upazila LIKE '%লক্ষীপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Lalbagh'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lalmai'
WHERE permanent_upazila LIKE '%লালমাই%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Lalmohan'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lalmonirhat Sadar'
WHERE permanent_upazila LIKE '%লালমনিরহাট%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Lalpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lama'
WHERE permanent_upazila LIKE '%লামা%'
   OR permanent_upazila LIKE '%লমা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Langadu'
WHERE permanent_upazila LIKE '%লংগদু%';

UPDATE education.adm_applicant SET permanent_upazila = 'Laxmichhari'
WHERE permanent_upazila LIKE '%লক্ষীছড়ি%'
   OR permanent_upazila LIKE '%লক্ষ্মীছড়ি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lohagara'
WHERE permanent_upazila LIKE '%লোহাগ%'
   OR permanent_upazila LIKE '%লোহাগড়া%'
   OR permanent_upazila LIKE '%লোহাগাড়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Lohajang'
WHERE permanent_upazila LIKE '%লৌহজং%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Madan'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Madarganj'
WHERE permanent_upazila LIKE '%মাদারগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Madaripur Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Madhabpur'
WHERE permanent_upazila LIKE '%মাধবপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Madhukhali'
WHERE permanent_upazila LIKE '%মধুখালী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Madhupur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Madhyanagar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Magura Sadar'
WHERE permanent_upazila LIKE '%মাগুরা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mahalchhari'
WHERE permanent_upazila LIKE '%মহাল%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Maheshpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Manda'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Manikchhari'
WHERE permanent_upazila LIKE '%মানিকছড়ি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Manikgonj Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Manirampur'
WHERE permanent_upazila LIKE '%মণিরামপুর%'
   OR permanent_upazila LIKE '%মনিরামপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Manpura'
WHERE permanent_upazila LIKE '%মনপুরা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mathbaria'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Matiranga'
WHERE permanent_upazila LIKE '%মাটি%গা%'
   OR permanent_upazila LIKE '%মাটিরাঙা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Matlab Dakshin'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Matlab Uttar'
WHERE permanent_upazila LIKE '%মতলব উত্তর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Meghna'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mehendiganj'
WHERE permanent_upazila LIKE '%মেহেন্দীগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Meherpur Sadar'
WHERE permanent_upazila LIKE '%মেহেরপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Melandaha'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mirpur'
WHERE permanent_upazila LIKE '%মিরপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mirsharai'
WHERE permanent_upazila LIKE '%মির%সরাই%'
   OR permanent_upazila LIKE '%মীরসরাই%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mirzaganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mirzapur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mithamain'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mithapukur'
WHERE permanent_upazila LIKE '%মিঠাপুকুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mohadevpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mohammadpur'
WHERE permanent_upazila LIKE '%মোহাম্মদপুর%'
   OR permanent_upazila LIKE '%মোহাম্মদ পুর%'
   OR permanent_upazila LIKE '%মুহম্মদপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mohanganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mohanpur'
WHERE permanent_upazila LIKE '%মোহনপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Moheshkhali'
WHERE permanent_upazila LIKE '%মহ%খালী%'
   OR permanent_upazila LIKE '%মহেশখাঁলী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mollahat'
WHERE permanent_upazila LIKE '%মোল্লাহাট%';

UPDATE education.adm_applicant SET permanent_upazila = 'Mongla'
WHERE permanent_upazila LIKE '%মোংলা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Monohardi'
WHERE permanent_upazila LIKE '%মনোহরদী%'
   OR permanent_upazila LIKE '%মনোহরদী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Monohargonj'
WHERE permanent_upazila LIKE '%মনোহরগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Morrelganj'
WHERE permanent_upazila LIKE '%মোড়েলগঞ্জ%'
   OR permanent_upazila LIKE '%মোড়লগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Motihar'
WHERE permanent_upazila LIKE '%মতিহার%';

UPDATE education.adm_applicant SET permanent_upazila = 'Motijheel'
WHERE permanent_upazila LIKE '%মতিঝিল%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Moulvibazar Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mugda'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mujibnagar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Muksudpur'
WHERE permanent_upazila LIKE '%মুকসুদপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Muktagachha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Muladi'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Munshiganj Sadar'
WHERE permanent_upazila LIKE '%মুন্স%';

UPDATE education.adm_applicant SET permanent_upazila = 'Muradnagar'
WHERE permanent_upazila LIKE '%মুরাদনগর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Mymensingh Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nabiganj'
WHERE permanent_upazila LIKE '%নবীগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nabinagar'
WHERE permanent_upazila LIKE '%নবীনগর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nachol'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nagarkanda'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nagarpur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nageshwari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Naikhongchhari'
WHERE permanent_upazila LIKE '%নাইক্ষ্যংছড়ি%'
   OR permanent_upazila LIKE '%নাইক্ষ্য%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nakla'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nalchity'
WHERE permanent_upazila LIKE '%নলছিটি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Naldanga'
WHERE permanent_upazila LIKE '%নলডাঙ্গা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nalitabari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nandail'
WHERE permanent_upazila LIKE '%নান্দাইল%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nandigram'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nangalkot'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Naniarchar'
WHERE permanent_upazila LIKE '%নানি%চর%'
   OR permanent_upazila LIKE '%নানুয়ারচর%'
   OR permanent_upazila LIKE '%নানীয়ার চর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Naogaon Sadar'
WHERE permanent_upazila LIKE '%নওগা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Narail Sadar'
WHERE permanent_upazila LIKE '%নড়াইল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Narayanganj Sadar'
WHERE permanent_upazila LIKE '%নারায়নগঞ্জ%'
   OR permanent_upazila LIKE '%নারায়গঞ্জ%'
   OR permanent_upazila LIKE '%নারায়ণগ্ঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Naria'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Narsingdi Sadar'
WHERE permanent_upazila LIKE '%নরসিংদী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nasirnagar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Natore Sadar'
WHERE permanent_upazila LIKE '%নাটোর%'
   OR permanent_upazila LIKE '%নাটোর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nawabganj'
WHERE permanent_upazila LIKE 'নবাবগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nazirpur'
WHERE permanent_upazila LIKE '%নজিরপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nesarabad (Swarupkati)'
WHERE permanent_upazila LIKE '%নেছারাবাদ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Netrokona Sadar'
WHERE permanent_upazila LIKE '%নেত্রকোনা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'New Market'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Niamatpur'
WHERE permanent_upazila LIKE '%নিয়ামতপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Nikli'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Nilphamari Sadar'
WHERE permanent_upazila LIKE '%নীলফামারী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Noakhali Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Osmani Nagar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Paba'
WHERE permanent_upazila LIKE '%পাবা%'
   OR permanent_upazila LIKE '%পবা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Pabna Sadar'
WHERE permanent_upazila LIKE '%পাবনা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Pahartali'
WHERE permanent_upazila LIKE '%পাহাড়তলী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Paikgachha'
WHERE permanent_upazila LIKE '%পাইকগাছা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Pakundia'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Palash'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Palashbari'
WHERE permanent_upazila LIKE '%পলাশবাড়ী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Pallabi'
WHERE permanent_upazila LIKE '%পল্লবী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Paltan'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Panchagarh Sadar'
WHERE permanent_upazila LIKE '%পঞ্চগড়%';

UPDATE education.adm_applicant SET permanent_upazila = 'Panchbibi'
WHERE permanent_upazila LIKE '%পাঁচবিবি%'
   OR permanent_upazila LIKE '%পাঁবিবি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Panchhari'
WHERE permanent_upazila LIKE '%পানছড়ি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Panchlaish'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Pangsha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Parbatipur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Parshuram'
WHERE permanent_upazila LIKE '%পরশুরাম%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Patenga'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Patgram'
WHERE permanent_upazila LIKE '%পাটগ্রাম%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Patharghata'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Patiya'
WHERE permanent_upazila LIKE '%পটিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Patnitala'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Patuakhali Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Pekua'
WHERE permanent_upazila LIKE '%পেকুয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Phulbari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Phulchhari'
WHERE permanent_upazila LIKE '%ফুলছড়ি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Phulpur'
WHERE permanent_upazila LIKE '%ফুলপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Pirgachha'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Pirganj'
WHERE permanent_upazila LIKE '%পীরগঞ্জ%'
   OR permanent_upazila LIKE '%পিরগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Pirojpur Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Porsha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Pubail'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Purbadhala'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Puthia'
WHERE permanent_upazila LIKE '%পুঠিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Raiganj'
WHERE permanent_upazila LIKE '%রায়গঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Raipur'
WHERE permanent_upazila LIKE '%রায়পুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Raipura'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rajapur'
WHERE permanent_upazila LIKE '%রাজাপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rajarhat'
WHERE permanent_upazila LIKE '%রাজারহাট%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rajasthali'
WHERE permanent_upazila LIKE '%রাজস্থলী%'
   OR permanent_upazila LIKE '%রাজস্হলী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Rajbari Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Rajnagar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rajoir'
WHERE permanent_upazila LIKE '%রাজৈর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rajpara'
WHERE permanent_upazila LIKE '%রাজপাড়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ramganj'
WHERE permanent_upazila LIKE '%রামগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ramgarh'
WHERE permanent_upazila LIKE '%রামগড়%'
   OR permanent_upazila LIKE '%রামগর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ramgati'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ramna'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rampal'
WHERE permanent_upazila LIKE '%রামপাল%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Rampura'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ramu'
WHERE permanent_upazila LIKE '%রামু%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Rangabali'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rangamati Sadar'
WHERE permanent_upazila LIKE '%রাঙ্গামাটি%'
   OR permanent_upazila LIKE '%র%মাটি%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rangpur Sadar'
WHERE permanent_upazila LIKE '%রংপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rangunia'
WHERE permanent_upazila LIKE '%রাঙ্গুন%'
   OR permanent_upazila LIKE '%রাংগুনিয়া%'
   OR permanent_upazila LIKE '%রাংগুনীয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Raninagar'
WHERE permanent_upazila LIKE '%রাণীনগর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ranisankail'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Raozan'
WHERE permanent_upazila LIKE '%রাউজান%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rowangchhari'
WHERE permanent_upazila LIKE '%রোয়াংছড়ি%'
   OR permanent_upazila LIKE '%রোয়াংছড়ি%'
   OR permanent_upazila LIKE '%রোয়াংছড়ি%'
   OR permanent_upazila LIKE '%রোয়াছড়ি%'
   OR permanent_upazila LIKE '%রোয়ারংছড়ি%'
   OR permanent_upazila LIKE '%রোয়াছড়ি%'
   OR permanent_upazila LIKE '%রোয়াংছাড়ি%'
   OR permanent_upazila LIKE '%রোয়াংচড়ি%'
   OR permanent_upazila LIKE '%রোয়াংছাড়ি%'
   OR permanent_upazila LIKE '%রােয়াংছড়ি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Rowmari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ruma'
WHERE permanent_upazila LIKE '%রুমা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rupganj'
WHERE permanent_upazila LIKE '%রূপগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rupnagar'
WHERE permanent_upazila LIKE '%রূপনগর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Rupsha'
WHERE permanent_upazila LIKE '%রূপসা%'
   OR permanent_upazila LIKE '%রুপসা%'
   OR permanent_upazila LIKE '%রুপসা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sabujbagh'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sadarghat'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sadarpur'
WHERE permanent_upazila LIKE '%সদরপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sadullapur'
WHERE permanent_upazila LIKE '%সাদুল্লা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sakhipur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Saltha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sandwip'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Santhia'
WHERE permanent_upazila LIKE '%সাঁথিয়া%'
   OR permanent_upazila LIKE '%সাথিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sapahar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sarail'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sarankhola'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sariakandi'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sarishabari'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Satkania'
WHERE permanent_upazila LIKE '%সাতকানিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Satkhira Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Saturia'
WHERE permanent_upazila LIKE '%সাটুরিয়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Savar'
WHERE permanent_upazila LIKE '%সাভার%';

UPDATE education.adm_applicant SET permanent_upazila = 'Senbagh'
WHERE permanent_upazila LIKE '%সেনবাগ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shah Ali'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shah Makhdum'
WHERE permanent_upazila LIKE '%শাহমখদুম%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shahbag'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shahjadpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shahjahanpur'
WHERE permanent_upazila LIKE '%সাজাহান পূর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shahrasti'
WHERE permanent_upazila LIKE '%শাহরাস্তি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shailkupa'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shajahanpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shalikha'
WHERE permanent_upazila LIKE '%শালিখা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shalla'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shantiganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shariatpur Sadar'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sharsha'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shayestaganj'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sher-E-Bangla Nagar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sherpur'
WHERE permanent_upazila LIKE '%শেরপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sherpur Sadar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shibchar'
WHERE permanent_upazila LIKE '%শিবচর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shibganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shibpur'
WHERE permanent_upazila LIKE '%শিবপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shilpanchal'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shivalaya'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Shyamnagar'
WHERE permanent_upazila LIKE '%শ্যামনগ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Shyampur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Siddhirganj'
WHERE permanent_upazila LIKE '%সিদ্ধরগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Singair'
WHERE permanent_upazila LIKE '%সিংগাইর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Singra'
WHERE permanent_upazila LIKE '%সিংড়া%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sirajdikhan'
WHERE permanent_upazila LIKE '%সিরাজদিখান%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sirajganj Sadar'
WHERE permanent_upazila LIKE '%সিরাজগঞ্জ%'
   OR permanent_upazila LIKE '%সিসাজগঞ্জ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sitakunda'
WHERE permanent_upazila LIKE '%সীতাকুন্ড%'
   OR permanent_upazila LIKE '%সিতাকুন্ড%'
   OR permanent_upazila LIKE '%শীতাকুন্ড%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sonadangha'
WHERE permanent_upazila LIKE '%সোনাডাঙ্গা%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sonagazi'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sonaimuri'
WHERE permanent_upazila LIKE '%সোনাইমুড়ী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sonargaon'
WHERE permanent_upazila LIKE '%সোনারগাঁ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sonatola'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sreebardi'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sreemangal'
WHERE permanent_upazila LIKE '%শ্রীমঙ্গল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sreenagar'
WHERE permanent_upazila LIKE '%শ্রীনগর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sreepur'
WHERE permanent_upazila LIKE '%শ্রীপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Subarnachar'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sughatta'
WHERE permanent_upazila LIKE '%সাঘাটা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sujanagar'
WHERE permanent_upazila LIKE '%সুজানগর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sunamganj Sadar'
WHERE permanent_upazila LIKE '%সুনামগঞ্জ সদর%'
   OR permanent_upazila LIKE '%সুনামগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Sundarganj'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sutrapur'
WHERE permanent_upazila LIKE '%সূত্রাপুর%'
   OR permanent_upazila LIKE '%সুত্রাপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Syedpur'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Sylhet Sadar'
WHERE permanent_upazila LIKE '%সিলেট%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tahirpur'
WHERE permanent_upazila LIKE '%তাহিরপুর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tala'
WHERE permanent_upazila LIKE '%তাল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Taltali'
WHERE permanent_upazila LIKE '%তালতলী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tangail Sadar'
WHERE permanent_upazila LIKE '%টাঙ্গাইল%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tanore'
WHERE permanent_upazila LIKE '%তানোর%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tara Khanda'
WHERE permanent_upazila LIKE '%তারাকান্দা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Taraganj'
WHERE permanent_upazila LIKE '%তারাগঞ্জ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Tarail'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Tarash'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Tazumuddin'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tejgaon'
WHERE permanent_upazila LIKE '%তেজগাঁও%';

UPDATE education.adm_applicant SET permanent_upazila = 'Teknaf'
WHERE permanent_upazila LIKE '%টেকনাফ%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Terokhada'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Tetulia'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Thakurgaon Sadar'
WHERE permanent_upazila LIKE '%ঠাকুরগাঁও%';

UPDATE education.adm_applicant SET permanent_upazila = 'Thanchi'
WHERE permanent_upazila LIKE '%থানচি%'
   OR permanent_upazila LIKE '%থানছি%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Titas'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tongi'
WHERE permanent_upazila LIKE '%টংগী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Tongibari'
WHERE permanent_upazila LIKE '%টংগীবাড়ী%'
   OR permanent_upazila LIKE '%টংগিবাড়ী%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Trishal'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Tungipara'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Turag'
WHERE permanent_upazila LIKE '%তুরাগ%';

UPDATE education.adm_applicant SET permanent_upazila = 'Ukhiya'
WHERE permanent_upazila LIKE '%উখিয়া%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ulipur'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Ullahpara'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Uttara East'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Uttara West'
-- WHERE permanent_upazila LIKE '%%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Uttarkhan'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Vatara'
WHERE permanent_upazila LIKE '%ভাটারা%';

UPDATE education.adm_applicant SET permanent_upazila = 'Wari'
WHERE permanent_upazila LIKE '%ওয়ারী%';

UPDATE education.adm_applicant SET permanent_upazila = 'Wazirpur'
WHERE permanent_upazila LIKE '%উজিরপুর%';

-- UPDATE education.adm_applicant SET permanent_upazila = 'Zajira'
-- WHERE permanent_upazila LIKE '%%';

UPDATE education.adm_applicant SET permanent_upazila = 'Zakiganj'
WHERE permanent_upazila LIKE '%জকিগঞ্জ%';

INSERT INTO education.aca_academic_class(id, created_at, created_by, updated_by, updated_at, institution_id, class_id, class_group, section, session, status)
SELECT id, created, created_by, last_updated_by, updated, institution, academic_class, groups,
       CASE
           WHEN section = 1 THEN 'A'
           WHEN section = 2 THEN 'B'
           WHEN section = 3 THEN 'C'
           WHEN section = 4 THEN 'D'
           WHEN section = 5 THEN 'E'
       END AS section,
       session, status
FROM qeis.academic_class_promotion;

UPDATE education.aca_academic_class
SET class_group = CASE
    WHEN class_group = '0' THEN ''
    WHEN class_group = '1' THEN 'Science'
    WHEN class_group = '2' THEN 'Business'
    WHEN class_group = '3' THEN 'Humanities'
    WHEN class_group = '11' THEN 'General Electronics'
    WHEN class_group = '12' THEN 'Wood Working'
    WHEN class_group = '13' THEN 'Computer & Information Technology'
    WHEN class_group = '14' THEN 'General Mechanics'
    WHEN class_group = '15' THEN 'Wielding & Fabrication'
    ELSE class_group
END;

UPDATE education.aca_academic_class
SET status = CASE
    WHEN status = 1 THEN 0
    WHEN status = 2 THEN 1
    WHEN status = 3 THEN 2
    WHEN status = 9 THEN 3
    ELSE status
END;

UPDATE education.aca_academic_class
SET release_status = 0;

INSERT INTO education.aca_subject_details(id, created_at, created_by, updated_by, updated_at, subject, subject_type, academic_class, remarks)
SELECT id, created, created_by, last_updated_by, updated, subject, subject_type, class_promotion, remarks
FROM qeis.academic_class_subjects;

INSERT INTO education.adm_student(id, created_at, created_by, updated_by, updated_at, applicant, student_id, status, institution_id, residence_id)
SELECT id, created, created_by, last_updated_by, updated, applicant, student_id, status, institution, residence
FROM qeis.adm_student;

UPDATE education.adm_student AS ads
SET residence_name = res.name
FROM qeis.residence AS res
WHERE ads.residence_id = res.id;

UPDATE education.adm_student AS ads
SET current_academic_class_id = (
SELECT MAX(p.class_promotion)
FROM  qeis.academic_class_promotion_details AS p
WHERE ads.id = p.student
GROUP BY p.student);

UPDATE education.adm_student
SET institution_name = CASE
   WHEN institution_id = 1 THEN 'Quantum Cosmo School (Primary)'
   WHEN institution_id = 2 THEN 'Quantum Cosmo School (High)'
   WHEN institution_id = 3 THEN 'Quantum Cosmo College'
   WHEN institution_id = 4 THEN 'Bodhichara Public School'
   WHEN institution_id = 5 THEN 'Quantum Cosmo School (Girls)'
   WHEN institution_id = 6 THEN 'Quantum Cosmo School (Vocational)'
   WHEN institution_id = 7 THEN 'Quantum Cosmo School (Basic Trade)'
END;

UPDATE education.adm_student
SET status = CASE
     WHEN status = '1' THEN 'Current Quanta'
     WHEN status = '2' THEN 'Higher Studying Quanta'
     WHEN status = '3' THEN 'Ex Quanta'
     WHEN status = '4' THEN 'Dropout Quanta'
     WHEN status = '5' THEN 'Suspended Quanta'
     WHEN status = '6' THEN 'Expelled Quanta'
     ELSE status
END;

UPDATE education.adm_student AS s
SET current_academic_class_info =
    CASE
        WHEN (ac.class_group = '' AND ac.section IS NULL) THEN CONCAT(c.name, ' (', ac.session, ')')
        WHEN (ac.class_group = '') THEN CONCAT(c.name, ', ', ac.section, ' (', ac.session, ')')
        WHEN (ac.section IS NULL) THEN CONCAT(c.name, ', ', ac.class_group, ' (', ac.session, ')')
        ELSE CONCAT(c.name, ', ', ac.class_group, ', ', ac.section, ' (', ac.session, ')')
    END
FROM education.aca_academic_class AS ac JOIN education.aca_class AS c ON c.id = ac.class_id
WHERE s.current_academic_class_id = ac.id;

UPDATE education.adm_applicant
    SET selected = true,
        admitted = true
WHERE id IN (SELECT applicant FROM education.adm_student);

INSERT INTO education.adm_guardian(id, created_at, created_by, updated_by, updated_at, email, name_en, name_bn, nationality, relation, primary_phone, secondary_phone, student)
SELECT g.id, created, g.created_by, last_updated_by, updated, email, name, name_bengali, 0, guardian_type, mobile1, mobile2, s.id
FROM qeis.adm_guardian AS g
JOIN education.adm_student AS s ON s.applicant = g.applicant;

UPDATE education.adm_guardian AS g
    SET image_path = CASE
        WHEN g.relation = '1' THEN a.father_image_path
        WHEN g.relation = '2' THEN a.mother_image_path
        ELSE g.image_path
    END
FROM qeis.adm_applicant AS a
JOIN education.adm_student AS s
ON s.applicant = a.id
WHERE g.student = s.id;

UPDATE education.adm_guardian
SET relation = CASE
    WHEN relation = '0' THEN '-Please Select-'
    WHEN relation = '1' THEN 'Father'
    WHEN relation = '2' THEN 'Mother'
    WHEN relation = '3' THEN 'Brother'
    WHEN relation = '4' THEN 'Sister'
    WHEN relation = '5' THEN 'Grandfather'
    WHEN relation = '6' THEN 'Grandmother'
    WHEN relation = '7' THEN 'Uncle'
    WHEN relation = '8' THEN 'Aunt'
    WHEN relation = '9' THEN 'Nephew'
    WHEN relation = '10' THEN 'Niece'
    WHEN relation = '11' THEN 'Cousin'
    WHEN relation = '12' THEN 'Stepfather'
    WHEN relation = '13' THEN 'Stepmother'
    WHEN relation = '14' THEN 'Stepbrother'
    WHEN relation = '15' THEN 'Stepsister'
    WHEN relation = '16' THEN 'Brother-in-Law'
    WHEN relation = '17' THEN 'Sister-in-Law'
    WHEN relation = '18' THEN 'Husband'
    WHEN relation = '19' THEN 'Wife'
    WHEN relation = '20' THEN 'Son'
    WHEN relation = '21' THEN 'Daughter'
    WHEN relation = '22' THEN 'Father-in-Law'
    WHEN relation = '23' THEN 'Mother-in-Law'
    WHEN relation = '24' THEN 'Son-in-Law'
    WHEN relation = '25' THEN 'Daughter-in-Law'
    WHEN relation = '26' THEN 'Teacher'
    WHEN relation = '27' THEN 'Student'
    WHEN relation = '28' THEN 'Friend'
    WHEN relation = '29' THEN 'Classmate'
    WHEN relation = '30' THEN 'Doctor'
    WHEN relation = '31' THEN 'Colleague'
    WHEN relation = '32' THEN 'Neighbor'
    WHEN relation = '33' THEN 'Relative'
    WHEN relation = '34' THEN 'Knows as'
    WHEN relation = '35' THEN 'Foster Father'
    WHEN relation = '36' THEN 'Foster Mother'
    WHEN relation = '37' THEN 'Nominated Guardian'
    WHEN relation = '9999' THEN 'Others'
END;

UPDATE education.adm_guardian AS e
SET indentity_type = CASE
    WHEN q.birth_reg_no IS NOT NULL AND q.birth_reg_no != '' THEN 'Birth Certificate'
    WHEN q.national_id IS NOT NULL AND q.national_id != '' THEN 'NID'
END
FROM qeis.adm_guardian AS q
WHERE q.id = e.id;

UPDATE education.adm_guardian AS e
SET indentity_number = CASE
    WHEN e.indentity_type = 'Birth Certificate' THEN q.birth_reg_no
    WHEN e.indentity_type = 'NID' THEN q.national_id
END
FROM qeis.adm_guardian AS q
WHERE q.id = e.id;

INSERT INTO education.adm_student_document(student_id, document_id)
SELECT student, document
FROM qeis.adm_student_necessary_documents;

INSERT INTO education.aca_class_students(id, created_at, created_by, updated_at, updated_by, status, remarks, student, academic_class)
SELECT id, created, created_by, updated, last_updated_by, student_status, remarks, student, class_promotion
FROM qeis.academic_class_promotion_details;

UPDATE education.aca_class_students
SET status = CASE
     WHEN status = '1' THEN 'Current Quanta'
     WHEN status = '2' THEN 'Higher Studying Quanta'
     WHEN status = '3' THEN 'Ex Quanta'
     WHEN status = '4' THEN 'Dropout Quanta'
     WHEN status = '5' THEN 'Suspended Quanta'
     WHEN status = '6' THEN 'Expelled Quanta'
     WHEN status = '11' THEN 'Promoted'
     WHEN status = '12' THEN 'Not Promoted'
     WHEN status = '13' THEN 'Shifted'
     ELSE status
END;

INSERT INTO education.aca_student_shift(id, created_at, created_by, updated_at, updated_by, student_id, student_name, shift_from_institution, shift_to_institution, shift_from_class_info, shift_to_class_info, shift_by, cause)
SELECT id, action_time, changed_by, action_time, changed_by, student_id, student_name, institution, institution, clazz, clazz, changed_by_name, cause
FROM qeis.student_shift_log;

UPDATE education.aca_student_shift AS s
SET shift_from_class_info = SPLIT_PART(shift_from_class_info, ' - ', 1), shift_to_class_info = SPLIT_PART(shift_to_class_info, ' - ', 2);

UPDATE education.aca_student_shift AS ash
SET student_row_id = (
    SELECT id
    FROM education.adm_student AS ads
    WHERE ads.student_id = ash.student_id
);

INSERT INTO education.aca_exam_marks(id, created_at, created_by, updated_by, updated_at, academic_class, subject, exam, status)
SELECT id, created, created_by, last_updated_by, updated, class_promotion, subject, exam, status
FROM qeis.academic_exam_marks;

INSERT INTO education.aca_student_marks(id, created_at, created_by, updated_at, updated_by, student_id, marks, mark_sheet)
SELECT id, NOW(), 1, NOW(), 1, student, marks, exam_marks
FROM qeis.academic_exam_marks_details;

UPDATE education.aca_student_marks AS a
SET quantaa_id = b.student_id
FROM education.adm_student AS b
Where a.student_id = b.id;

UPDATE education.aca_student_marks AS a
SET student_name = c.name_en
FROM education.adm_student AS b
         JOIN education.adm_applicant AS c
              ON b.applicant = c.id
WHERE a.student_id = b.id;

INSERT INTO education.aca_attendance(id, created_at, created_by, updated_by, updated_at, type, date, institution, status, academic_class)
SELECT id,
       CASE
           WHEN created IS NULL THEN NOW()
           ELSE created
       END AS created,
       created_by, last_updated_by, updated, attendance_type, attendance_date, institution, status, class_promotion
FROM qeis.attendance
WHERE attendance_type IN ('11', '12');

UPDATE education.aca_attendance
SET institution = CASE
    WHEN institution = '1' THEN 'Quantum Cosmo School (Primary)'
    WHEN institution = '2' THEN 'Quantum Cosmo School (High)'
    WHEN institution = '3' THEN 'Quantum Cosmo College'
    WHEN institution = '4' THEN 'Bodhichara Public School'
    WHEN institution = '5' THEN 'Quantum Cosmo School (Girls)'
    WHEN institution = '6' THEN 'Quantum Cosmo School (Vocational)'
    WHEN institution = '7' THEN 'Quantum Cosmo School (Basic Trade)'
    ELSE institution
END;

UPDATE education.aca_attendance
SET type = CASE
   WHEN type = '11' THEN 'Academic Attendance'
   WHEN type = '12' THEN 'Refreshment Class Attendance'
   ELSE type
END;

UPDATE education.aca_attendance AS a
SET class_info =
    CASE
        WHEN (ac.class_group = '' AND ac.section IS NULL) THEN CONCAT(c.name, ' (', ac.session, ')')
        WHEN (ac.class_group = '') THEN CONCAT(c.name, ', ', ac.section, ' (', ac.session, ')')
        WHEN (ac.section IS NULL) THEN CONCAT(c.name, ', ', ac.class_group, ' (', ac.session, ')')
        ELSE CONCAT(c.name, ', ', ac.class_group, ', ', ac.section, ' (', ac.session, ')')
    END
FROM education.aca_academic_class AS ac
         JOIN education.aca_class AS c ON c.id = ac.class_id
WHERE a.academic_class = ac.id;

INSERT INTO education.aca_attendance_status(id, created_at, created_by, updated_at, updated_by, student_id, attendance_status, absent_cause, attendance_sheet)
SELECT id, created, created_by, updated, created_by, student, attendance_status, absent_cause, attendance
FROM qeis.attendance_details
WHERE attendance IN (
    SELECT id FROM education.aca_attendance
    );

UPDATE education.aca_attendance_status
SET attendance_status = CASE
    WHEN attendance_status = '1' THEN 'Present'
    WHEN attendance_status = '4' THEN 'Treatment in Shafian'
    WHEN attendance_status = '5' THEN 'Higher Treatment'
    WHEN attendance_status = '11' THEN 'Sport'
    WHEN attendance_status = '12' THEN 'Program'
    WHEN attendance_status = '17' THEN 'Residence'
    WHEN attendance_status = '19' THEN 'Leave'
    WHEN attendance_status = '99' THEN 'Absent'
    ELSE attendance_status
END;

UPDATE education.aca_attendance_status AS a
SET quantaa_id   = s.student_id,
    student_name = ap.name_en
FROM education.adm_student As s
         JOIN education.adm_applicant AS ap
              ON ap.id = s.applicant
WHERE s.id = a.student_id;

INSERT INTO education.res_residence(id, created_at, created_by, updated_by, updated_at, name, code, phone, email, responsible_person, address, remarks)
SELECT id, created, created_by, last_updated_by, updated, name, code, phone, email, responsible_person, address, remarks
FROM qeis.residence;

INSERT INTO education.res_hall(id, created_at, created_by, updated_by, updated_at, name, code, remarks, residence)
SELECT id, created, created_by, last_updated_by, updated, hall_name, hall_code, remarks, residence
FROM qeis.residence_hall;

INSERT INTO education.res_room(id, created_at, created_by, updated_by, updated_at, code, capacity, description, hall)
SELECT id, created, created_by, last_updated_by, updated, room_code, seat_capacity, description, residence_hall
FROM qeis.residence_room;

INSERT INTO education.res_seat(id, created_at, created_by, updated_by, updated_at, seat_code, status, seat_description, room_id)
SELECT id, created, created_by, last_updated_by, updated, seat_code, status, description, room
FROM qeis.residence_room_seat;

UPDATE education.res_seat
SET status = CASE
    WHEN status = '1' THEN 'free'
    WHEN status = '7' THEN 'inactive'
    WHEN status = '9' THEN 'off'
    WHEN status = '3' THEN 'booked'
    ELSE status
END;

INSERT INTO education.res_room_seats(room_id, seats_id)
SELECT room_id, id
FROM education.res_seat;

INSERT INTO education.res_attendance(id, created_at, created_by, updated_by, updated_at, type, date, institution, status, academic_class)
SELECT id,
       CASE
           WHEN created IS NULL THEN NOW()
           ELSE created
           END AS created,
       created_by, last_updated_by, updated, attendance_type, attendance_date, institution, status, class_promotion
FROM qeis.attendance
WHERE attendance_type IN ('1', '5', '5', '7');

UPDATE education.res_attendance
SET institution = CASE
                      WHEN institution = '1' THEN 'Quantum Cosmo School (Primary)'
                      WHEN institution = '2' THEN 'Quantum Cosmo School (High)'
                      WHEN institution = '3' THEN 'Quantum Cosmo College'
                      WHEN institution = '4' THEN 'Bodhichara Public School'
                      WHEN institution = '5' THEN 'Quantum Cosmo School (Girls)'
                      WHEN institution = '6' THEN 'Quantum Cosmo School (Vocational)'
                      WHEN institution = '7' THEN 'Quantum Cosmo School (Basic Trade)'
                      ELSE institution
    END;

UPDATE education.res_attendance
SET type = CASE
               WHEN type = '1' THEN 'Residence Attendance'
               WHEN type = '5' THEN 'Breakfast Attendance'
               WHEN type = '6' THEN 'Lunch Attendance'
               WHEN type = '7' THEN 'Dinner Attendance'
               ELSE type
    END;

UPDATE education.res_attendance AS a
SET class_info =
        CASE
            WHEN (ac.class_group = '' AND ac.section IS NULL) THEN CONCAT(c.name, ' (', ac.session, ')')
            WHEN (ac.class_group = '') THEN CONCAT(c.name, ', ', ac.section, ' (', ac.session, ')')
            WHEN (ac.section IS NULL) THEN CONCAT(c.name, ', ', ac.class_group, ' (', ac.session, ')')
            ELSE CONCAT(c.name, ', ', ac.class_group, ', ', ac.section, ' (', ac.session, ')')
            END
FROM education.aca_academic_class AS ac
         JOIN education.aca_class AS c ON c.id = ac.class_id
WHERE a.academic_class = ac.id;

INSERT INTO education.res_attendance_status(id, created_at, created_by, updated_at, updated_by, student_id, attendance_status, absent_cause, attendance_sheet)
SELECT id, created, created_by, updated, created_by, student, attendance_status, absent_cause, attendance
FROM qeis.attendance_details
WHERE attendance IN (
    SELECT id FROM education.res_attendance
);

UPDATE education.res_attendance_status
SET attendance_status = CASE
                            WHEN attendance_status = '1' THEN 'Present'
                            WHEN attendance_status = '4' THEN 'Treatment in Shafian'
                            WHEN attendance_status = '5' THEN 'Higher Treatment'
                            WHEN attendance_status = '11' THEN 'Sport'
                            WHEN attendance_status = '12' THEN 'Program'
                            WHEN attendance_status = '17' THEN 'Residence'
                            WHEN attendance_status = '19' THEN 'Leave'
                            WHEN attendance_status = '99' THEN 'Absent'
                            ELSE attendance_status
    END;

UPDATE education.res_attendance_status AS a
SET quantaa_id = s.student_id,
    student_name = ap.name_en
FROM education.adm_student As s
         JOIN education.adm_applicant AS ap
              ON ap.id = s.applicant
WHERE s.id = a.student_id;

SELECT setval('education.aca_academic_class_id_seq', (SELECT MAX(id) FROM education.aca_academic_class));
SELECT setval('education.aca_subject_details_id_seq', (SELECT MAX(id) FROM education.aca_subject_details));
SELECT setval('education.aca_academic_class_id_seq', (SELECT MAX(id) FROM education.aca_academic_class));
SELECT setval('education.aca_class_students_id_seq', (SELECT MAX(id) FROM education.aca_class_students));
SELECT setval('education.aca_student_marks_id_seq', (SELECT MAX(id) FROM education.aca_student_marks));
SELECT setval('education.aca_attendance_id_seq', (SELECT MAX(id) FROM education.aca_attendance));
SELECT setval('education.aca_attendance_status_id_seq', (SELECT MAX(id) FROM education.aca_attendance_status));
SELECT setval('education.adm_exam_center_id_seq', (SELECT MAX(id) FROM education.adm_exam_center));
SELECT setval('education.adm_applicant_id_seq', (SELECT MAX(id) FROM education.adm_applicant));
SELECT setval('education.adm_student_id_seq', (SELECT MAX(id) FROM education.adm_student));
SELECT setval('education.adm_guardian_id_seq', (SELECT MAX(id) FROM education.adm_guardian));
SELECT setval('education.adm_document_id_seq', (SELECT MAX(id) FROM education.adm_document));
SELECT setval('education.aca_class_id_seq', (SELECT MAX(id) FROM education.aca_class));
SELECT setval('education.aca_subject_id_seq', (SELECT MAX(id) FROM education.aca_subject));
SELECT setval('education.aca_subject_type_id_seq', (SELECT MAX(id) FROM education.aca_subject_type));
SELECT setval('education.aca_exam_id_seq', (SELECT MAX(id) FROM education.aca_exam));
SELECT setval('education.aca_institution_id_seq', (SELECT MAX(id) FROM education.aca_institution));
SELECT setval('education.aca_student_shift_id_seq', (SELECT MAX(id) FROM education.aca_student_shift));
SELECT setval('education.aca_exam_marks_id_seq', (SELECT MAX(id) FROM education.aca_exam_marks));
SELECT setval('education.res_residence_id_seq', (SELECT MAX(id) FROM education.res_residence));
SELECT setval('education.res_residence_class_id_seq', (SELECT MAX(id) FROM education.res_residence_class));
SELECT setval('education.res_hall_id_seq', (SELECT MAX(id) FROM education.res_hall));
SELECT setval('education.res_room_id_seq', (SELECT MAX(id) FROM education.res_room));
SELECT setval('education.res_seat_id_seq', (SELECT MAX(id) FROM education.res_seat));
SELECT setval('education.res_attendance_id_seq', (SELECT MAX(id) FROM education.res_attendance));
SELECT setval('education.res_attendance_status_id_seq', (SELECT MAX(id) FROM education.res_attendance_status));