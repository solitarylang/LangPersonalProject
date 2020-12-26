/**
 * @Author ：lang
 * @Date ：Created in 2020/8/13 2:53 PM
 * @Description ：
 * @Version :
 */
public class test1 {
    public static void main(String[] args) {
        String str = "    ,nvl(((t2.1d_1dt_7leads/t2.1d_1all_leads + \n" +
                "           t2.1d_2dt_7leads/t2.1d_2all_leads + t2.1d_3dt_7leads/t2.1d_3all_leads +\n" +
                "           t2.1d_4dt_7leads/t2.1d_4all_leads + t2.1d_5dt_7leads/t2.1d_5all_leads + \n" +
                "           t2.1d_6dt_7leads/t2.1d_6all_leads + t2.1d_7dt_7leads/t2.1d_7all_leads)/7),0) as except_7d_ltv_1d_number\n" +
                "    ,nvl((t2.before_120d_7d_7leads/t2.before_120d_7d_all_leads),0) as except_7d_ltv_7d_number\n" +
                "    ,nvl((t2.before_120d_30d_7leads/t2.before_120d_30d_all_leads),0) as except_7d_ltv_30d_number\n" +
                "    ,nvl((t2.before_120d_mtd_7leads/t2.before_120d_mtd_all_leads),0) as except_7d_ltv_mtd_number";

        System.out.println(str.toLowerCase());
    }
}
