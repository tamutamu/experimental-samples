select count(*) - count(distinct c.CountryCode) as 'count of duplicated country code' from city c