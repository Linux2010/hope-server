package cc.mrbird.febs.job.util;

import cc.mrbird.febs.common.utils.SpringContextUtil;
import cc.mrbird.febs.job.domain.Job;
import cc.mrbird.febs.job.domain.JobLog;
import cc.mrbird.febs.job.service.JobLogService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.*;

/**
 * 定时任务
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {

    //private ExecutorService service = Executors.newSingleThreadExecutor();
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    private ExecutorService service = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Object object = context.getMergedJobDataMap().get(Job.JOB_PARAM_KEY);
        Job scheduleJob = new Job();
        BeanUtils.copyProperties(object,scheduleJob);

        // 获取spring bean
        JobLogService scheduleJobLogService = SpringContextUtil.getBean(JobLogService.class);

        JobLog jobLog = new JobLog();
        jobLog.setJobId(scheduleJob.getJobId());
        jobLog.setBeanName(scheduleJob.getBeanName());
        jobLog.setMethodName(scheduleJob.getMethodName());
        jobLog.setParams(scheduleJob.getParams());
        jobLog.setCreateTime(new Date());

        long startTime = System.currentTimeMillis();

        try {
            // 执行任务
            log.info("任务准备执行，任务ID：{}", scheduleJob.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(),
                    scheduleJob.getParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes(times);
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(JobLog.JOB_SUCCESS);

            log.info("任务执行完毕，任务ID：{} 总共耗时：{} 毫秒", scheduleJob.getJobId(), times);
        } catch (Exception e) {
            log.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setTimes(times);
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(JobLog.JOB_FAIL);
            jobLog.setError(StringUtils.substring(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.saveJobLog(jobLog);
        }
    }
}
